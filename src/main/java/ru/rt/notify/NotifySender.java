package ru.rt.notify;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import ru.rt.conf.Conf;
import ru.rt.dict.Params;
import static ru.rt.dict.Params.LOGGER;
import ru.rt.oms.Attribute;
import ru.rt.oms.Attributes;
import ru.rt.oms.Fault;
import ru.rt.oms.NotificationOrderItems;
import ru.rt.oms.NotificationResponse;
import ru.rt.oms.OMSOrderNotificationWebService;
import ru.rt.oms.OrderParties;
import ru.rt.oms.OrderStatus;
import ru.rt.oms.OrderStatusNotification;
import ru.rt.oms.Party;
import ru.rt.oms.Result;
import ru.rt.utils.SOAPLogger;
import ru.rt.utils.Utils;

public class NotifySender {
    
    private final Conf conf;
    private final SOAPLogger logger;
    private final OMSOrderNotificationWebService bssService;
    private final static String URL_BLOCKED_TEMP = "UrlBlockedTemp";
    private final static String URL_BLOCKED_FINAL = "UrlBlockedFinal";
    private final static String NOTIFICATION_NOT_SEND = "NotificationNotSend";
    private final static String EVENT = "Event";
    
    public NotifySender(OMSOrderNotificationWebService bssService, Conf conf, SOAPLogger logger) {
	this.conf = conf;
	this.logger = logger;
	this.bssService = bssService;
    }
    
    //Формирование очереди
    public NotifyHelper getNotifyHelper(NotifyQueue queue, ConcurrentHashMap<String, NotifyHelper> helpers){	
	String bssUrl = queue.getBssUrl();			
	String omsId = queue.getOmsId();
	String orderId = queue.getOrderId();
	String key = omsId + orderId + bssUrl;
	NotifyHelper notifyHelper = helpers.get(omsId + orderId + bssUrl);
	if (notifyHelper == null){
	    notifyHelper = new NotifyHelper(bssUrl, omsId, orderId, conf.getServiceGuid());
	    //LOGGER.log(Level.INFO, "{0} {1} Создан новый обработчик OrderId={2}", new Object[]{bssUrl, omsId, orderId});
	    helpers.put(key, notifyHelper);
	}
	return notifyHelper;
    }
    
    //цикл отправки нотификаций по конкретному заказу
    public void doAsyncWork(NotifyHelper notifyHelper, ConcurrentHashMap<String, NotifyHelper> helpers, ConcurrentHashMap<String, String> notifySending){	
	notifyHelper.setRun();
	Set<String> errors = new HashSet<>();
	final String bssUrl = notifyHelper.getBssUrl();
	final String omsId = notifyHelper.getOmsId();
	final String orderId = notifyHelper.getOrderId();
	String notifyId = null;
	//LOGGER.log(Level.INFO, "{0} {1} Выполняется запуск обработчика! Попытка #{2}. Серия #{3}.", new Object[]{bssUrl, omsId, notifyHelper.getCountErr(), notifyHelper.getCountLockDown()});
	logger.toServerLog(orderId, omsId, notifyId, bssUrl, EVENT, "Выполняется запуск обработчика! Попытка " + notifyHelper.getCountErr() + " Серия " + notifyHelper.getCountLockDown());
	try {
	    //получаем список нотификаций, которые нужно отправить
	    //LOGGER.log(Level.INFO, "{0} {1} Загружаем список нотификаций ...", new Object[]{bssUrl, omsId});
	    List<NotifyData> notifies = findNotifyForSend(bssUrl, omsId, errors); 
	    //LOGGER.log(Level.INFO, "{0} {1} Найдено {2} нотификаций", new Object[]{bssUrl, omsId, notifies.size()});
	    logger.toServerLog(orderId, omsId, notifyId, bssUrl, EVENT, "Найдено " + notifies.size() + " нотификаци(й)");
	    if (!errors.isEmpty() || notifies.isEmpty()) {
		return;
	    }
	    
	    //обрабатываем список нотификаций заказа
	    for(NotifyData notifyData : notifies){
		notifyId = notifyData.getNotifyId();
		if (!notifySending.contains(notifyId)){
		    asyncSendNotify(notifyData, notifySending, errors);
		} else {
		    LOGGER.log(Level.INFO, "{0} {1} Попытка повторной отправки нотификаций id ={2}", new Object[]{bssUrl, omsId, notifyId});
		}
		if (!errors.isEmpty()) break;	//если возникла ошибка при отправке нотификации, то не отправлять остальные нотификации этого заказа
	    }
        } catch (Exception ex) {
	    String shortMsg = getShortMsg(ex.getMessage());
	    LOGGER.log(Level.SEVERE, shortMsg);
	    errors.add(shortMsg);
	} finally {	    
            if (!errors.isEmpty()){
		//LOGGER.log(Level.INFO, "{0} {1} Обработчик завершил работу. Обнаружено ошибок {2}", new Object[]{bssUrl, omsId, errors.size()});
		logger.toServerLog(orderId, omsId, notifyId, bssUrl, EVENT, "Обработчик завершил работу. Обнаружено ошибок " + errors.size());
		notifyHelper.getErrors().addAll(errors);
		if (isNeedLockDown(notifyHelper)){ //проверка на финальную блокировку
		    notifyHelper.setIsLockDown(true);		    
		    //mailAlert.sendMailAlert(notifyHelper.getErrors());
		    //LOGGER.log(Level.INFO, "{0} {1} Установлена финальная блокировка из-за множественных ошибок!", new Object[]{bssUrl, omsId});
		    logger.toServerLog(orderId, omsId, notifyId, bssUrl, URL_BLOCKED_FINAL, "Установлена финальная блокировка из-за множественных ошибок!");
		} else 
		    if (isNeedLock(notifyHelper)) {			
			//временно блокируем данный сервис из-за повторяющихся ошибок
			//LOGGER.log(Level.INFO, "{0} {1} Установлена временная блокировка из-за повторяющихся ошибок!", new Object[]{bssUrl, omsId});			
			logger.toServerLog(orderId, omsId, notifyId, bssUrl, URL_BLOCKED_TEMP, "Установлена временная блокировка из-за повторяющихся ошибок!");
			notifyHelper.setIsErrBlock(); //установка блокировки сервиса из-за ошибок
		    }
	    } else {
		//LOGGER.log(Level.INFO, "{0} {1} Обработчик успешно завершил работу", new Object[]{bssUrl, omsId});
		logger.toServerLog(orderId, omsId, notifyId, bssUrl, EVENT, "Обработчик успешно завершил работу");
		notifyHelper.setSuccessfullyStatus();
		notifyHelper.clearCountLockDown(); //раз обработчик корректно отработал, то нужно сбросить счётчик финальной блокировки 
	    }
	    notifyHelper.setStop(); //снимаем блокировку признака выполнения сервиса
        }
    }
    
    /**
     * Подготовка нотификации к отправке в BSS
     * @param notifyData
     * @param bssService
     * @param errors 
     */
    private void asyncSendNotify(NotifyData notifyData, ConcurrentHashMap<String, String> notifySending, Set<String> errors){
	OrderStatusNotification notification = notifyData.getNotification();
	final String omsId = notification.getOrder().getOrderOMSId();
	final String orderId = notification.getOrder().getOrderId();
	final String bssURL = notifyData.getUrl();
	final String notifId = notifyData.getNotifyId();
	try {
	    //Params.LOGGER.log(Level.INFO, "{0} {1} Старт отправки нотификации id={2}", new Object[]{bssURL, omsId, notifId});
	    logger.toServerLog(orderId, omsId, notifId, bssURL, EVENT, "Старт отправки нотификации");
	    Map<String, String> resultMap = sendNotify(notification, notifId, bssURL);	
	    
	    final String resultCode = resultMap.get(Params.RESULT_CODE);
	    final String resultTxt = resultMap.get(Params.RESULT_TEXT);	
	    final String status = resultMap.get(Params.STATUS);
	    String errMsg;
	    if ("0".equals(resultCode)){
		errMsg = "";
		//Params.LOGGER.log(Level.INFO, "{0} {1} Нотификация id={2} успешно отправлена!", new Object[]{bssURL, omsId, notifId});
		logger.toServerLog(orderId, omsId, notifId, bssURL, EVENT, "Нотификация успешно отправлена");
		notifySending.put(notifId, notifId);
	    } else {
		errMsg = resultTxt;
		errors.add("Не удалось отправить нотификацию: " + bssURL + " " + omsId + " " + getShortMsg(errMsg));
		//Params.LOGGER.log(Level.INFO, "{0} {1} Не удалось отправить нотификацию: CODE={2} TEXT={3}", new Object[]{bssURL, omsId, resultCode, getShortMsg(errMsg)});
		logger.toServerLog(orderId, omsId, notifId, bssURL, NOTIFICATION_NOT_SEND, "Не удалось отправить нотификацию! CODE=" + resultCode + "TEXT=" + getShortMsg(errMsg));
	    }
	    updateNotifyStatus(notifId, status, omsId, errMsg, errors);
	} catch (Exception ex){
	    String errShortMsg = getShortMsg(ex.getMessage());
	    //Params.LOGGER.log(Level.INFO, "{0} {1} Ошибка отправки асинхронной нотификации: {2}", new Object[]{bssURL, omsId, errShortMsg});
	    logger.toServerLog(orderId, omsId, notifId, bssURL, EVENT, "Ошибка отправки асинхронной нотификации: " + errShortMsg);
	    errors.add("Ошибка отправки асинхронной нотификации: " + errShortMsg);
	}
    }
    
    private Map<String, String> sendNotify(OrderStatusNotification notification, final String notifId, final String bssURL){	
	final String omsId = notification.getOrder().getOrderOMSId();
	final String orderId = notification.getOrder().getOrderId();
	try {
	    checkNotification(notification);	//очистка equipment от null

	    //final String dateRequest = Utils.getCurrentDateAsString();
	    logger.notificationToLog(notification, notifId, Utils.getCurrentDateAsString());
	    NotificationResponse response = bssService.notifyOrderStatus(notification);	//отправка
	    //final String dateResponse = Utils.getCurrentDateAsString();
	    logger.responseToLog(response, orderId, notification.getRequestId(), Utils.getCurrentDateAsString());
	    
	    if (response == null || response.getResult() == null){
		return createResult("6", "BSS did not return Result!", Params.STATUS_REJECTED);
	    }

	    Result result = response.getResult();
	    if (result.getResultCode() == null || result.getResultCode().isEmpty()){
		return createResult("6", "BSS did not return ResultCode!", Params.STATUS_REJECTED);
	    }
	    if ("0".equals(result.getResultCode().trim())){
		return createResult("0", Params.STATUS_COMPLETED, Params.STATUS_COMPLETED);
	    }
	    return createResult(result.getResultCode(), result.getResultText(), Params.STATUS_REJECTED);
	} catch (Exception ex){
	    String errMsg = getShortMsg(ex.getMessage());
	    String status = "";
	    if (ex.getCause() instanceof Fault){
		Params.LOGGER.log(Level.INFO, "{0} {1} Fault exception!", new Object[]{bssURL, omsId});
		status = Params.STATUS_REJECTED;
	    } else 
		if (ex.getCause() instanceof SocketTimeoutException){
		    errMsg = getShortMsg(ex.getCause().getMessage());
		    status = Params.STATUS_SO_TIMEOUT;
		}
	    return createResult("2", errMsg, status);
	}
    }           

    public void updateNotifyStatus(String notifyId, String status, String omsId, String errMsg, Set<String> errors){
	if (notifyId == null){
	    Params.LOGGER.log(Level.INFO, "{0} UpdateNotifyStatus: nothing to update because notifyId = NULL!", omsId);
	    return;
	}
	String sql = "UPDATE ilink.notify_async_bss SET \"Status\" = ? , \"RespError\" = ?, \"DateSend\" = ? WHERE \"OmsId\" = ? AND \"Status\" not in ('COMPLETED', 'REJECTED')";
	if (!Params.STATUS_REJECTED.equals(status)){	//если статус REJECTED, то обновляем все нотификации этим статусом
	    sql = sql + " AND \"NotifyId\" = ?";	//иначе только эту нотификацию
	    //Params.LOGGER.log(Level.INFO, "{0} Обновление нотификации NotifyId={1} Status={2}", new Object[]{omsId, notifyId, status});
	}
	int countRow = 0;
	try (Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
	    ps.setString(1, status);
	    ps.setString(2, errMsg);
	    ps.setTimestamp(3, Utils.getCurrentTimeStamp());
	    ps.setString(4, omsId);
	    if (!Params.STATUS_REJECTED.equals(status)){
		ps.setString(5, notifyId);
	    }
	    countRow = ps.executeUpdate();
	    if (countRow == 0){
		Params.LOGGER.log(Level.SEVERE, "{0} Нотификация(и) не найдена в базе данных!", new Object[]{omsId, notifyId});
	    } 
	    /*
	    else {
		Params.LOGGER.log(Level.INFO, "{0} Обновлено нотификаций {1}", new Object[]{omsId, countRow});
	    }
	    */
	} catch (Exception ex) {
	    if (ex instanceof SQLException){
		SQLException sqlEx = (SQLException) ex;
		Params.LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{sqlEx.getSQLState(), ex.getMessage()});
		Params.LOGGER.log(Level.SEVERE, "sql={0}, Status={1}, NotifyId={2}", new Object[]{sql, status, notifyId});
		errors.add("SQL error: " + sqlEx.getSQLState() + " " + ex.getMessage());
	    } else{
		errors.add(ex.getMessage());
		Params.LOGGER.log(Level.SEVERE, null, new Object[]{ex.getMessage()});
	    }
	    safeCompletedNotifyStatus(notifyId, status);
	}
    }
    
    private List<NotifyData> findNotifyForSend(String bssUrl, String omsId, Set<String> errors){
	List<NotifyData> notifications = new ArrayList<>();
	final String sql = "SELECT * FROM ilink.notify_async_bss WHERE \"BSS_URL\" = ? AND \"OmsId\" = ? AND \"ServiceGuid\" = ? AND \"Status\" NOT IN ('COMPLETED', 'REJECTED') ORDER BY \"DateCtreate\"";
	try(Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
	    ps.setString(1, bssUrl);
	    ps.setString(2, omsId);
	    ps.setString(3, conf.getServiceGuid());
	    ResultSet resultSet = ps.executeQuery();
	    while (resultSet.next()) {
		notifications.add(loadNotifyData(resultSet, errors));
	    }
	} catch (SQLException ex) {
	    Params.LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});
	    errors.add("SQL error: " + ex.getMessage());
	}	
	return notifications;
    }
    
    private NotifyData loadNotifyData(ResultSet resultSet, Set<String> errors){
	NotifyData notifyData = new NotifyData();
	OrderStatusNotification orderNotification;
	try {
	    notifyData.setOrderId(resultSet.getString(1));
	    notifyData.setOmsId(resultSet.getString(2));
	    notifyData.setNotifyId(resultSet.getString(4));
	    notifyData.setDateCreate(resultSet.getTimestamp(5));
	    String xml = resultSet.getString(6);
	    notifyData.setXmlData(xml);
	    notifyData.setUrl(resultSet.getString(7));
	    notifyData.setStatus(resultSet.getString(8));
	    notifyData.setErrMsg(resultSet.getString(9));
	    notifyData.setDateSend(resultSet.getTimestamp(10));	    
	    notifyData.setServiceUID(resultSet.getString(11));
	    JAXBContext jaxbContext = JAXBContext.newInstance(OrderStatusNotification.class);
	    Unmarshaller um = jaxbContext.createUnmarshaller();
	    orderNotification = (OrderStatusNotification) um.unmarshal(new StringReader(xml));
	    notifyData.setNotification(orderNotification);
	    return notifyData;
	} catch (SQLException | JAXBException ex) {
	    errors.add(ex.getMessage());
	    Params.LOGGER.log(Level.SEVERE, "Load Notify Data error! ", ex); 
	}
	return notifyData;
    }
	
    private void safeCompletedNotifyStatus(String notifyId, String status){
	if (!Params.STATUS_COMPLETED.equals(status)) return;
	
	Params.LOGGER.log(Level.INFO, "{0} Из-за проблем с базой данных выполняется сохранение статуса нотификации на диск!", new Object[]{notifyId});
	File dir = new File(Conf.NOTIFY_SAFE_FOLDER);
        if (!dir.exists()){
            dir.mkdirs();
        }
	StringBuilder content = new StringBuilder();
	content.append("{notifyId:").append(notifyId).append(", status:").append(status).append("}");
	StringBuilder fileName = new StringBuilder();
        fileName.append(Conf.NOTIFY_SAFE_FOLDER).append(notifyId).append(".notify");
	try {
	    Files.write(Paths.get(fileName.toString()), content.toString().getBytes("utf-8"), 
	    StandardOpenOption.CREATE,
	    StandardOpenOption.TRUNCATE_EXISTING);
	    Params.LOGGER.log(Level.INFO, "{0} Нотификация сохранена на диск!", new Object[]{notifyId});
	} catch (UnsupportedEncodingException ex) {
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	}
    }
    
    private Map<String, String> createResult(String code, String text, String status){
	Map<String, String> result = new HashMap<>();
	result.put(Params.RESULT_CODE, code); 
	result.put(Params.RESULT_TEXT, text);
	result.put(Params.STATUS, status);
	return result;
    }
    
    /**
     * Очищает список equipment от null
     * @param notification 
     */
    private void checkNotification(OrderStatusNotification notification){	
	OrderStatus orderStatus = notification.getOrder();
	if (orderStatus == null) return;
	checkAttributes(orderStatus.getOrderAttributes());
	checkNotificationOrderItems(orderStatus.getOrderItems());
	checkParties(orderStatus.getOrderParties());	
    }
    
    private void checkNotificationOrderItems(NotificationOrderItems noi){
	if (noi == null) return;
	noi.getOrderItem().forEach(oi->checkParties(oi.getOrderItemParties()));
    }
    
    private void checkParties(OrderParties orderParties){
	if (orderParties == null) return;
	orderParties.getOrderPartyOrOrderAttachment()
	    .forEach(ob->{
		if (ob instanceof Party){
		    Party party = (Party) ob;		
		    checkAttributes(party.getPartyAttributes());
		}
	    });	
    }
    
    private void checkAttributes(Attributes attributes){
	if (attributes == null) return;
	attributes.getAttribute().forEach(atr->checkAttribute(atr));
    }
    
    private void checkAttribute(Attribute attribute){
	if (attribute == null) return;	
	if ("equipmentList".equalsIgnoreCase(attribute.getName().trim())){	    
	    List<Object> clearEquip = clearNullEquipment(attribute.getContent());
	    attribute.getContent().clear();
	    attribute.getContent().addAll(clearEquip);	    
	}
    }
    
    private List<Object> clearNullEquipment(List<Object> obj){		
	obj.removeIf(Objects::isNull);
	return obj.stream().filter(o -> {
	    if (o instanceof String){		
		String equipment = ((String) o).trim();		
		if (equipment.contains("[equipment: null]")){		    
		    return false;
		}
	    }
	    return true;
	})
	.collect(Collectors.toList());
    }
    
    private boolean isNeedLock(NotifyHelper notifyHelper){
	return notifyHelper.addErrorCount() > conf.getCountErrForLock() ;
    }
    
    private boolean isNeedLockDown(NotifyHelper notifyHelper){
	return notifyHelper.getCountLockDown() > conf.getCountAttemptsForLockdown() ;
    } 
    
    private String getShortMsg(String longMsg){
	String shortMsg = "";
	if (longMsg != null){
	    shortMsg = longMsg.substring(0, Math.min(conf.getErrMinLenght(), longMsg.length()));
	}
	return shortMsg;
    }
}

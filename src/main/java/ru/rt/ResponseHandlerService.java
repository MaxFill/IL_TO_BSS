package ru.rt;

import com.comptel.soa._2011._02.instantlink.Notification;
import com.comptel.soa._2011._02.instantlink.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.rt.oms.*;
import ru.rt.conv.ResponseConvertor;
import ru.rt.dict.Params;
import ru.rt.notify.NotifyPersist;
import ru.rt.utils.Utils;
import javax.ejb.EJB;
import javax.jws.WebService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import ru.rt.conf.Conf;
import ru.rt.notify.NotifyTimer;
import ru.rt.utils.SOAPLogger;

import static ru.rt.utils.Utils.WRONG_REQUEST;

/**
 * Служба принимает сообщения (нотификации) от IL
 * Основную логику отправки нотификации в CRM реализует класс NotifyTimer
 * @author Maksim.Filatov
 */
@WebService(serviceName = "ResponseHandlerService",
    portName = "ResponseHandlerWithAckPort",
    endpointInterface = "com.comptel.soa._2011._02.instantlink.ResponseHandler", 
    targetNamespace = "http://soa.comptel.com/2011/02/instantlink",
    wsdlLocation = "WEB-INF/wsdl/ResponseHandlerService.wsdl")
public class ResponseHandlerService {
    private static final String SEND2BSS = "SEND2BSS";    
    private static final String NOT_FOUND = "NOT_FOUND";
    
    @EJB private NotifyPersist notifPersist;
    @EJB private ResponseConvertor responseConv;
    @EJB private SOAPLogger logger;
    @EJB private NotifyTimer notifyTimer;
    @EJB private Conf conf;
    
    /**
     * Нотификация может прийти как непостредственно из IL так и из нотификационной NEI
     * Если вызов пришёл из NEI, то выполняется только сохранение нотификации в базу данных
     * Если вызов из IL, то выполняется и сохранение и отправка нотификации в BSS
     * @param iLresponse
     * @return 
     */
    public String handleResponse(Response iLresponse){
	if (iLresponse == null) {
            throw new IllegalArgumentException("ERROR: no any data for processing in handleResponse!");
        }
	Response response = sanitizer(iLresponse);
	Map<String, String> params = responseConv.respParamsToMap(response);
	final String BSS_URL = params.get(Params.CALLBACK_ENDPOINT);
	final String requestId = params.get(Params.REQUEST_ID);
	final String taskId = params.get(Params.TASK_ID);	
	Set<String> errors = new HashSet<>();
	if (StringUtils.isBlank(BSS_URL) || NOT_FOUND.equals(BSS_URL)){
	    errors.add("Parameter CALLBACK_ENDPOINT is incorrect! BSS URL is incorrect!");
	    return makeResult(errors, taskId);
	}
	
	// если вызов пришёл из NEI, то выполняется только сохранение нотификации в базу данных, и выходим
	if (Params.NEI.equals(response.getResponseHeader().getReqUser())){ 
	    Params.LOGGER.log(Level.INFO, "------------------ Получена нотификация из NEI -----------------------");
	    // формируем нотификацию из полученных параметров
	    OrderStatusNotification notification = responseConv.toOrderStatusNotification(params, errors);    
	    checkNotification(notification);
	    // сохраняем нотификацию в базу данных
	    String omsId = notification.getOrder().getOrderOMSId();
	    String orderId = notification.getOrder().getOrderId();
	    final String logInfo = orderId + " " + omsId;
	    String serviceGuid = findServiceGuid(params, orderId, omsId);
	    logger.requestToLog(response, orderId, requestId, Utils.getCurrentDateAsString());
	    String status = notifPersist.checkRejectedStatusByOrder(omsId, orderId);
	    //String serviceGuid = findServiceGuid(params, orderId, omsId);
	    final String notifyId = notifPersist.saveNotify(notification, taskId, BSS_URL, status, serviceGuid, logInfo, errors);
	    if (Params.STATUS_REJECTED.equals(status)){
		Params.LOGGER.log(Level.INFO, "{0} Нотификация {1} помечена как REJECTED потому что по заказу OmsId={2} имеются нотификации в статусе REJECTED!", new Object[]{logInfo, notifyId, omsId});
	    } else {
		notifyTimer.doWork();
	    }
	    //Params.LOGGER.log(Level.INFO, "{0} ------------------- Response was sending to NEI ASYNC ----------------- ", new Object[]{logInfo});
	    return makeResult(errors, notifyId);
	}
	
	Params.LOGGER.log(Level.INFO, "-------------------- Получена нотификация из IL ----------------------");
	// если вызов из IL, то выполняется отправка нотификации в BSS
	OrderStatusNotification notification;
	if (taskId == null){
	    // формируем нотификацию из полученных параметров	    
	    notification = responseConv.toOrderStatusNotification(params, errors);
	    // сохраняем нотификацию в базу данных
	    String omsId = notification.getOrder().getOrderOMSId();
	    String orderId = notification.getOrder().getOrderId();
	    if (orderId == null){
		Params.LOGGER.log(Level.INFO, "Получена кривая нотификация! OrderId=null, OmsId=null ");
		return "1";
	    }
	    final String logInfo = orderId + " " + omsId;
	    logger.requestToLog(response, orderId, requestId, Utils.getCurrentDateAsString());
	    String serviceGuid = findServiceGuid(params, orderId, omsId);	    
	    String status = notifPersist.checkRejectedStatusByOrder(omsId, orderId);
	    String notifyId = notifPersist.saveNotify(notification, taskId, BSS_URL, status, serviceGuid, logInfo, errors);
	    if (Params.STATUS_REJECTED.equals(status)){
		Params.LOGGER.log(Level.INFO, "{0} Нотификация {1} помечена как REJECTED потому что по заказу OmsId={2} имеются нотификации в статусе REJECTED!", new Object[]{logInfo, notifyId, omsId});
	    } else {
		notifyTimer.doWork();
	    }
	}
	return "0";
    }

    /**     
     * Отправка в CRM ранее сохранённой в базе данных нотификации
     * Параметр SEND2BSS - используется в нотификациях через стандартный механизм нотификаций IL. 
     * Из логики IL отправляется MESSAGE_ID=SEND2BSS и MESSAGE=номер задачи (нотфикации), например, 306_5
     * @param iLnotification
     * @throws Fault 
     */
    public void handleNotification(Notification iLnotification) throws Fault {
	Notification notification = sanitizer(iLnotification);
	if (!SEND2BSS.equals(notification.getMessageId())){ //если параметр не задан то ничего не делаем!
	    /*
	    TFault faultData = new TFault();
	    faultData.setResultCode(WRONG_REQUEST);
	    faultData.setMessage("For send notification need add in messageId parameter SEND2BSS, and need add notifyId in messageText!");
	    thow new Fault("IL_TO_BSS: Parameter SEND2BSS required! RequestId=" + notification.getRequestId() + " OrderNo=" + notification.getOrderNo(), faultData);    
	    */
	    return;
	}
	final String notifyId = notification.getMessage(); //идентификатор для поиска нотификации в базе данных
	if (notifyId == null){
	    throw createFault("Notification not found by notifyId = NULL !"); 
	}
    }
    
    /* *** privates *** */    

    private String makeResult(Set<String> errors, String notifyId){
	String codeResult;
	if (errors.isEmpty()){
	    codeResult = "0";
	    errors.add(Params.STATUS_COMPLETED);
	} else {
	    codeResult = "2";
	}
	Map<String, String> resultMap = Utils.createDataResult(codeResult, errors, notifyId);
	ObjectMapper mapper = new ObjectMapper();
	String result = "";
	try {
	    result = mapper.writeValueAsString(resultMap);
	} catch (JsonProcessingException ex){
	    Params.LOGGER.log(Level.SEVERE, null, ex); 
	}
	return result;
    }
    
    /**
     * Получение ServiceGuid для нотификации. Сначала ищем в параметрах, если нет то в базе и если нет, то берём из конфигурации
     * @param params список параметров из IL
     * @param orderId 
     * @param omsId
     * @return 
     */
    private String findServiceGuid(Map<String, String> params, String orderId, String omsId){
	String logInfo = orderId + " " + omsId;
	String serviceGuid = params.get(Params.SERVICE_GUID);
	Params.LOGGER.log(Level.INFO, "{0} Получен параметр SERVICE_GUID = {1}", new Object[]{logInfo, serviceGuid});
	if (StringUtils.isNotBlank(serviceGuid)){
	    return serviceGuid;
	}	
	serviceGuid = conf.getServiceGuid();
	return serviceGuid;
    }

    private Response sanitizer(Response response){
	return response;
    }
	
    private Notification sanitizer(Notification notification){
	return notification;
    }
	
    private Fault createFault(String error){
	TFault faultData = new TFault();
	faultData.setResultCode(WRONG_REQUEST);
	faultData.setMessage("Notification for sending to BSS not found!");
	return new Fault(error, faultData);
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
}
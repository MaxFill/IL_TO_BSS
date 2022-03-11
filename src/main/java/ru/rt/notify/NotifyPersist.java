package ru.rt.notify;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang3.StringUtils;
import ru.rt.conf.Conf;
import ru.rt.dict.Params;
import static ru.rt.dict.Params.LOGGER;
import ru.rt.oms.*;
import ru.rt.utils.Utils;

/**
 * Сохранение асинхронной нотификаций в базе данных
 */
@Stateless
public class NotifyPersist {
    @EJB private Conf conf;    

    public String saveNotify(OrderStatusNotification notification, String taskId, String bssURL, String status, String serviceGuid, String logInfo, Set<String> errors) {
	if (taskId == null || notification == null) return null;
        //Params.LOGGER.log(Level.INFO, "{0} Start save notify id={1}", new Object[]{logInfo, taskId});
	final String id = addNotifyInDB(notification, taskId, bssURL, status, serviceGuid, errors);
        if (id == null){
	    Params.LOGGER.log(Level.INFO, "{0} Notify id={1} save failed!", new Object[]{logInfo, taskId});
	}
	return id;
    }
    
    //сброс статусов у нотификации
    public Set<String> clearNotifyStatus(NotifyData notifyData){
	String notifyId = notifyData.getNotifyId();
	String bssUrl = notifyData.getUrl();
	String omsId = notifyData.getOmsId();
	final String sql = "UPDATE ilink.notify_async_bss SET \"Status\" = '', \"RespError\" = '' WHERE \"NotifyId\" = ? ";
	Params.LOGGER.log(Level.INFO, "{0} Сброс статуса у нотификации id ={2}", new Object[]{bssUrl, omsId, notifyId});
	Set<String> msgs = new HashSet<>();
	try (Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
	    ps.setString(1, notifyId);
	    ps.executeUpdate();
	} catch (SQLException ex) {
	    Params.LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});
	    msgs.add("При выполнении операции возникла ошибка: " + ex.getSQLState() + " " + ex.getMessage());
	}
	return msgs;
    }
    
    //сброс статусов нотификаций
    public int clearNotifiesStatus(String bssUrl, String omsId, Set<String> errors){
	final String sql = "UPDATE ilink.notify_async_bss SET \"Status\" = '', \"RespError\" = '' WHERE \"BSS_URL\" = ? AND \"OmsId\" = ? AND \"Status\" = 'REJECTED'";
	int countRow = 0;
	//Params.LOGGER.log(Level.INFO, "{0} Сброс статусов нотификаций...", new Object[]{bssUrl, omsId});
	try (Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
	    ps.setString(1, bssUrl);
	    ps.setString(2, omsId);
	    countRow = ps.executeUpdate();
	} catch (SQLException ex) {
	    Params.LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});
	    errors.add("SQL error: " + ex.getSQLState() + " " + ex.getMessage());
	}
	//Params.LOGGER.log(Level.INFO, "{0}{1} Статус сброшен у {2} нотификаций", new Object[]{bssUrl, omsId, countRow});
	return countRow;
    }
    
    //обноление гуида сервиса в нотификациях
    public int updateServiceGuid(String serviceGuid, String bssUrl, String omsId, Set<String> errors){
	final String sql = "UPDATE ilink.notify_async_bss SET \"ServiceGuid\" = ?, \"RespError\" = '' WHERE \"BSS_URL\" = ? AND \"OmsId\" = ? ";
	int countRow = 0;
	//Params.LOGGER.log(Level.INFO, "{0} Обновление serviceGuid в нотификациях...", new Object[]{bssUrl, omsId});
	try (Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
	    ps.setString(1, serviceGuid);
	    ps.setString(2, bssUrl);
	    ps.setString(3, omsId);
	    countRow = ps.executeUpdate();
	} catch (SQLException ex) {
	    Params.LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});
	    errors.add("SQL error: " + ex.getSQLState() + " " + ex.getMessage());
	}
	//Params.LOGGER.log(Level.INFO, "{0}{1} Обновлено {2} нотификаций", new Object[]{bssUrl, omsId, countRow});
	return countRow;
    }        
    
    public void updateCompletedStatus(String notifyId) throws SQLException{
	if (notifyId == null){
	    Params.LOGGER.log(Level.INFO, "{0} UpdateNotifyStatus: nothing to update because notifyId = NULL!", notifyId);
	    return;
	}
	final String sql = "UPDATE ilink.notify_async_bss SET \"Status\" = 'COMPLETED', \"DateSend\" = ? WHERE \"NotifyId\" = ? ";	
	//Params.LOGGER.log(Level.INFO, "{0} Обновление нотификации Status=COMPLETED", new Object[]{notifyId});
	int countRow = 0;
	try (Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
	    ps.setTimestamp(1, Utils.getCurrentTimeStamp());
	    ps.setString(2, notifyId);
	    countRow = ps.executeUpdate();
	    if (countRow == 0){
		Params.LOGGER.log(Level.SEVERE, "{0} Нотификация(и) не найдена в базе данных!", new Object[]{notifyId});
	    } else {
		Params.LOGGER.log(Level.INFO, "{0} Запись статуса COMPLETED. Обновлено нотификаций = {1}", new Object[]{countRow});
	    }
	}
    }
    
    public String checkRejectedStatusByOrder(String omsId, String orderId){
	String status = "";
	final String sql = "SELECT \"OmsId\", \"Status\" FROM ilink.notify_async_bss WHERE \"OmsId\" = ? AND \"OrderId\" = ? AND \"Status\" = 'REJECTED' GROUP BY \"OmsId\", \"Status\"";
	try(Connection jdbcConnection = conf.getJdbcConnection();
	    PreparedStatement ps = jdbcConnection.prepareStatement(sql)) 
	{
	    if (jdbcConnection != null) {
		ps.setString(1, omsId);
		ps.setString(2, orderId);
		ResultSet resultSet = ps.executeQuery();
		if (resultSet.next()){	//если нашли такие, то выставляем статус в REJECTED
		    status = Params.STATUS_REJECTED;
		}
	    } 	
	} catch (SQLException ex) {
	    Params.LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});
	    Params.LOGGER.log(Level.SEVERE, "SQL {0}", sql);
	}
	return status;
    }            
    
    public List<NotifyData> findNotify(NotifyData filter){	
	return findNotifyByParams(filter.getUrl(), filter.getOrderId(), filter.getOmsId(), filter.getNotifyId(), filter.getStatus());
    }    
    
    public int delete(String notifyId){
	if (notifyId == null){
	    Params.LOGGER.log(Level.INFO, "{0} Nothing to delete, because notifyId = NULL!", notifyId);
	    return 0;
	}
	final String sql = "DELETE FROM ilink.notify_async_bss WHERE \"NotifyId\" = ? ";
	int countRow = 0;
	try (Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {	    
	    ps.setString(1, notifyId);
	    countRow = ps.executeUpdate();	    
	} catch (SQLException ex) {
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	}
	return countRow;
    }	
        
    public Set<NotifyQueue> getNotifyQueue(String bssUrl){
	final String sql = "SELECT  \"OmsId\", \"OrderId\" FROM ilink.notify_async_bss WHERE \"BSS_URL\" = ? AND \"ServiceGuid\" = ? GROUP BY \"BSS_URL\", \"OmsId\", \"Status\", \"OrderId\" HAVING \"Status\" not in ('COMPLETED', 'REJECTED') AND \"BSS_URL\" NOTNULL AND \"OmsId\" NOTNULL";
	Set<NotifyQueue> queue = new HashSet<>();
	try(Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
	    ps.setString(1, bssUrl);
	    ps.setString(2, conf.getServiceGuid());
	    ResultSet rs = ps.executeQuery();
	    while(rs.next()){			
		String omsId = rs.getString(1);
		String orderId = rs.getString(2);
		queue.add(new NotifyQueue(bssUrl, omsId, orderId));
	    }
	} catch (SQLException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	return queue;
    }
          
    public Set<String> getNotifyURLs(){
	final String sql = "SELECT \"BSS_URL\" FROM ilink.notify_async_bss GROUP BY \"BSS_URL\", \"Status\" HAVING \"Status\" not in ('COMPLETED', 'REJECTED') AND \"BSS_URL\" NOTNULL ";
	Set<String> bssList = new HashSet<>();
	try(Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
	    ResultSet rs = ps.executeQuery();
	    while(rs.next()){
		bssList.add(rs.getString(1));
	    }
	} catch (SQLException ex) {
	    LOGGER.log(Level.SEVERE, null, ex);
	}
	return bssList;
    }      
    
    public String loadNotifyXml(String notifyId){
	final String sql = "SELECT \"Notify\" FROM ilink.notify_async_bss WHERE \"NotifyId\" = '" + notifyId + "'";		
	Params.LOGGER.log(Level.INFO, "SQL = {0}", new Object[]{sql});  
	String xml = null;
	try(Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {	    
	    ResultSet resultSet = ps.executeQuery();
	    if (resultSet.next()) {
		xml = resultSet.getString(1);
	    }
	} catch (SQLException ex) {
	    Params.LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});          
	}
	return xml;
    }
	
    /* *** privates *** */
    
    private List<NotifyData> findNotifyByParams(String bssUrl, String orderId, String omsId, String notifyId, String status){
	List<NotifyData> notifications = new ArrayList<>();
	String sql = "SELECT * FROM ilink.notify_async_bss WHERE ";
	StringBuilder where = new StringBuilder();
	addWhereParam(where, "\"BSS_URL\"", bssUrl);
	addWhereParam(where, "\"OrderId\"", orderId);
	addWhereParam(where, "\"OmsId\"", omsId);
	addWhereParam(where, "\"NotifyId\"", notifyId);
	addWhereParam(where, "\"Status\"", status);
	where.append(" LIMIT 1000");
	sql = sql + where.toString();
	//Params.LOGGER.log(Level.INFO, "SQL = {0}", new Object[]{sql});  
	try(Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {	    
	    ResultSet resultSet = ps.executeQuery();
	    while (resultSet.next()) {
		notifications.add(loadNotifyData(resultSet, new HashSet<>()));
	    }
	} catch (SQLException ex) {
	    Params.LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), ex.getMessage()});          
	}	
	return notifications;
    }
        
    private void addWhereParam(StringBuilder sb, String param, String value){
	if (StringUtils.isNoneBlank(value)){
	    if (sb.length() > 0){
		sb.append(" AND ");
	    }
	    if (Params.STATUS_EMPTY.equals(value)){
		value = "";
	    }
	    sb.append(param).append(" = '").append(value).append("'");
	}
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
    
    private String addNotifyInDB(OrderStatusNotification osn, String taskId, String bssURL, String status, String serviceGuid, Set<String> errors) {
        OrderStatus orderStatus = osn.getOrder();	

        if (orderStatus == null) {
            throw new IllegalArgumentException("notify_BSS persist error: Order is NULL!");
        }
	
        final String orderId = orderStatus.getOrderId();
        final String omsId = orderStatus.getOrderOMSId();
	final String requestId = osn.getRequestId();
	
	//deleteOldNotifications(orderId, omsId, requestId); //очистка нотификаций в notify_persist
	
	final String sql = "INSERT INTO ilink.notify_async_bss (\"OrderId\", \"OmsId\", \"RequestId\", \"Notify\", \"NotifyId\", \"BSS_URL\", \"Status\", \"ServiceGuid\" ) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	
        StringWriter sw = new StringWriter();
        try {	    
	    JAXBContext jaxbContext = JAXBContext.newInstance(ru.rt.oms.OrderStatusNotification.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(osn, sw);
	    try (Connection conn = conf.getJdbcConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
		ps.setString(1, orderId);
		ps.setString(2, omsId);
		ps.setString(3, requestId);
		ps.setString(4, sw.toString());
		ps.setString(5, taskId);
		ps.setString(6, bssURL);
		ps.setString(7, status);
		ps.setString(8, serviceGuid);
		ps.executeUpdate();
	    } catch (SQLException ex) {
		String msg = getShortMsg(ex.getMessage());
		errors.add(msg);
		Params.LOGGER.log(Level.SEVERE, "SQL State {0} error: {1}", new Object[]{ex.getSQLState(), msg});
		//Params.LOGGER.log(Level.SEVERE, "SQL {0}", sql);
	    }
	    return taskId;
        } catch (JAXBException ex) {
	    errors.add(ex.getMessage());
            Params.LOGGER.log(Level.SEVERE, null, ex);
	    return null;
        }
    }
    
    private String getShortMsg(String longMsg){
	String shortMsg = "";
	if (longMsg != null){
	    shortMsg = longMsg.substring(0, Math.min(conf.getErrMinLenght(), longMsg.length()));
	}
	return shortMsg;
    }
}
package ru.rt.conv;

import com.comptel.soa._2011._02.instantlink.Parameter;
import com.comptel.soa._2011._02.instantlink.Response;
import ru.rt.dict.Params;
import ru.rt.utils.Utils;
import javax.ejb.Stateless;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ru.rt.oms.*;

/**
 * Конвертер параметров ответа IL в формат RT.FFASINCAPI
 * @author Maksim.Filatov
 */
@Stateless
public class ResponseConvertor {

    public OrderStatusNotification toOrderStatusNotification(Map<String, String> params, Set<String> errors) {
        //Params.LOGGER.log(Level.INFO, "{0} Start make OrderStatusNotification ...");
        OrderStatusNotification notification = new OrderStatusNotification();	
	try {
	    notification.setOrder(getOrderStatus(params));
	    notification.setOrderResult(getOrderResult(params));
	    notification.setOriginator(params.get(Params.ORIGINATOR));
	    notification.setReceiver(params.get(Params.RECEIVER));
	    String requestId;
	    if (params.containsKey(Params.TASK_ID)){
		requestId = params.get(Params.TASK_ID);
		//Params.LOGGER.log(Level.INFO, "{0} Make RequestId from param TASK_ID", new Object[]{logInfo, requestId});
	    } else {
		requestId = String.valueOf(params.get(Params.REQUEST_ID));
		//Params.LOGGER.log(Level.INFO, "{0} Make RequestId from param REQUEST_ID = {1}", new Object[]{logInfo, requestId});
	    }
	    notification.setRequestId(requestId);
	    //Params.LOGGER.log(Level.INFO, "{0} Finish make OrderStatusNotification!");
	} catch(ExecutionException | InterruptedException ex){
	    errors.add(ex.getMessage());
	    notification = null;
	}
        return notification;
    }

    /* *** privates *** */

    private OrderStatus getOrderStatus(Map<String, String> params) throws InterruptedException, ExecutionException {	
	OrderStatus orderStatus = new OrderStatus();
	/*
        CompletableFuture future1 = CompletableFuture.runAsync(()->orderStatus.setOrderNotifications(getOrderNotifications(Params.ORDER_NOTIF, params)));
        CompletableFuture future2 = CompletableFuture.runAsync(()->orderStatus.setOrderParties(getOrderParties(Params.ORDER, params)));
	CompletableFuture future3 = CompletableFuture.runAsync(()->orderStatus.setOrderItems(getNotificationOrderItems(Params.ORDER_ITEM, params)));
	CompletableFuture future4 = CompletableFuture.runAsync(()->getAttributes(Params.ORDER, params).ifPresent(orderStatus::setOrderAttributes));
	*/
	orderStatus.setOrderNotifications(getOrderNotifications(Params.ORDER_NOTIF, params));
        orderStatus.setOrderParties(getOrderParties(Params.ORDER, params));
	orderStatus.setOrderItems(getNotificationOrderItems(Params.ORDER_ITEM, params));
	getAttributes(Params.ORDER, params).ifPresent(orderStatus::setOrderAttributes);
	
	orderStatus.setOrderId(getOrderId(params));
        orderStatus.setOrderOMSId(getOrderOMSId(params));
        orderStatus.setOrderState(getOrderState(params));        
        orderStatus.setOrderComments(getComments(Params.ORDER, params));
	getCompletionDate(Params.ORDER, params).ifPresent(orderStatus::setOrderCompletionDate);
        getStartDate(Params.ORDER, params).ifPresent(orderStatus::setOrderStartDate);
	//CompletableFuture combinedFuture = CompletableFuture.allOf(future1, future2, future3, future4);	
	//combinedFuture.get();
        return orderStatus;
    }

    private OrderResult getOrderResult(Map<String, String> params) {
        String code = params.get(Params.ORDER_RESULT_CODE);
	String text = params.get(Params.ORDER_RESULT_TEXT);
	if (code == null && text == null) return null;
	OrderResult orderResult = new OrderResult();
        orderResult.setOrderResultCode(code);
        orderResult.setOrderResultText(text);
        return orderResult;
    }

    private OrderNotifications getOrderNotifications(final String key, Map<String, String> params) {
        //Params.LOGGER.log(Level.INFO, "start load OrderNotifications listsize={0}", getListSize(key, params));
	int listsize = getListSize(key, params);
	if (listsize == 0) return null;
	OrderNotifications orderNotifications = new OrderNotifications();
	orderNotifications.getOrderNotification().addAll(
	    IntStream.rangeClosed(1, listsize)
		.mapToObj(i -> getOrderNotification(key + "_" + i, params))
		.collect(Collectors.toList())
	);
	//Params.LOGGER.log(Level.INFO, "finish load OrderNotifications");
        return orderNotifications;
    }
    
    private OrderNotification getOrderNotification(final String itemKey, Map<String, String> params){
	//Params.LOGGER.log(Level.INFO, "start load OrderNotification key ={0}", itemKey);
	OrderNotification notification = new OrderNotification();
	getAttributes(itemKey, params).ifPresent(notification::setNotificationAttributes);
	getStatus(itemKey, params).ifPresent(notification::setNotificationStatus);
	getText(itemKey, params).ifPresent(notification::setNotificationText);
	getTimestamp(itemKey, params).ifPresent(notification::setNotificationTimestamp);
	//Params.LOGGER.log(Level.INFO, "finish load OrderNotification");
	return notification;
    }

    private NotificationOrderItems getNotificationOrderItems(final String key, Map<String, String> params) {
        int listsize = getListSize(key, params);
	if (listsize == 0) return null;
	NotificationOrderItems notificationOrderItems = new NotificationOrderItems();
	notificationOrderItems.getOrderItem().addAll(
	    IntStream.rangeClosed(1, listsize)
	    .mapToObj(i -> getNotificationOrderItem(key + "_" + i, params))
	    .collect(Collectors.toList())
	);
        return notificationOrderItems;
    }

    private NotificationOrderItem getNotificationOrderItem(final String itemKey, Map<String, String> params){
	NotificationOrderItem item = new NotificationOrderItem();
	item.setOrderItemAttributes(getInheritableAttributes(itemKey, params));
	item.setOrderItemParties(getOrderParties(itemKey, params));
	item.setOrderItemResult(getOrderItemResult(itemKey, params));
	getOrderItemInstanceId(itemKey, params).ifPresent(item::setOrderItemInstanceId);
	getId(itemKey, params).ifPresent(item::setOrderItemId);
	getOrderItemState(itemKey, params).ifPresent(item::setOrderItemState);
	getOrderItemAction(itemKey, params).ifPresent(item::setOrderItemAction);
	getOrderItemAppointmentId(itemKey, params).ifPresent(item::setOrderItemAppointmentId);
	return item;
    }
    
    private InheritableAttributes getInheritableAttributes(final String key, Map<String, String> params) {
        InheritableAttributes attributes = new InheritableAttributes();	
        final String itemkey = key + "_" + Params.ATTR;
	final int listsize = getListSize(itemkey, params);
	if (listsize == 0) return null;
	attributes.getAttribute().addAll(
	    IntStream.rangeClosed(1, listsize)
		.mapToObj(i -> getInheritableAttribute(itemkey + "_" + i, params))
		.collect(Collectors.toList())
	);
        return attributes;
    }

    private InheritableAttribute getInheritableAttribute(final String itemKey, Map<String, String> params){
	InheritableAttribute attribute = new InheritableAttribute();
	getIsChanged(itemKey, params).ifPresent(attribute::setIsChanged);
	getIsUpdated(itemKey, params).ifPresent(attribute::setIsUpdated);
	getIsInheritable(itemKey, params).ifPresent(attribute::setIsInheritable);
	getName(itemKey, params).ifPresent(attribute::setName);
	getAtrStatus(itemKey, params).ifPresent(attribute::setStatus);
	getAttributeRestriction(itemKey, params).ifPresent(attribute::setRestriction);
	getAtrValue(itemKey, params).ifPresent(v->attribute.getContent().add(v));
	loadEquipments(itemKey, params, attribute.getContent());
	return attribute;
    }
    
    private OrderItemResult getOrderItemResult(final String key, Map<String, String> params) {
        OrderItemResult itemResult = new OrderItemResult();
        getOptString(key, Params.RESULT_CODE, params).ifPresent(itemResult::setOrderItemResultCode);
        getOptString(key, Params.RESULT_TEXT, params).ifPresent(itemResult::setOrderItemResultText);
        return itemResult;
    }

    private OrderParties getOrderParties(final String prefix, Map<String, String> params) {        
	final String partyKey = prefix + "_" + Params.PARTY;
	OrderParties orderParties = new OrderParties();

	orderParties.getOrderPartyOrOrderAttachment().addAll(	
	    IntStream.rangeClosed(1, getListSize(partyKey, params))
		.mapToObj(i -> getParty(partyKey + "_" + i, params))
		.collect(Collectors.toList())
	);
	
	final String attacheKey = partyKey + "_" + Params.ATTACHMENT;
	orderParties.getOrderPartyOrOrderAttachment().addAll(
	    IntStream.rangeClosed(1, getListSize(attacheKey, params))
		.mapToObj(i -> getAttachment(attacheKey + "_" + i, params))
		.collect(Collectors.toList())
	);
        return orderParties;
    }

    private Attachment getAttachment(final String itemKey, Map<String, String> params){
	//Params.LOGGER.log(Level.INFO, "Start getAttachment itemKey = {0}", itemKey);
	Attachment attachment = new Attachment();
	getAttachmentType(itemKey, params).ifPresent(attachment::setAttachmentType);
	getAuthor(itemKey, params).ifPresent(attachment::setAuthor);
	getFileExtension(itemKey, params).ifPresent(attachment::setFileExtension);
	getFileName(itemKey, params).ifPresent(attachment::setFileName);
	getHeader(itemKey, params).ifPresent(attachment::setHeader);
	getURL(itemKey, params).ifPresent(attachment::setURL);
	getCreationDate(itemKey, params).ifPresent(attachment::setCreationDate);
	getRegister(itemKey, params).ifPresent(attachment::setAttachmentRegister);
	getAttributes(itemKey, params).ifPresent(attachment::setAttachmentAttributes);
	return attachment;
    }
    
    private Party getParty(String itemKey, Map<String, String> params){
	Party party = new Party();
	getAttributes(itemKey, params).ifPresent(party::setPartyAttributes);
	getId(itemKey, params).ifPresent(party::setPartyId);
	getName(itemKey, params).ifPresent(party::setPartyName);
	getPartyRole(itemKey, params).ifPresent(party::setPartyRole);
	return party;
    }    
    
    private Optional<Attributes> getAttributes(String key, Map<String, String> params) {
        final String indexKey = key + "_" + Params.ATTR;
	final int listsize = getListSize(indexKey, params);
	if (listsize == 0) {
	    return Optional.empty();
	}
	Attributes attributes = new Attributes();
	attributes.getAttribute().addAll(
	    IntStream.rangeClosed(1, listsize)
		.mapToObj(i -> getAttribute(i, indexKey, params))
		.collect(Collectors.toList())
	);
        return Optional.of(attributes);
    }
     
    private Attribute getAttribute(int index, String key, Map<String, String> params){	
	Attribute attribute = new Attribute();
	final String itemKey = key + "_" + index;
	//Params.LOGGER.log(Level.INFO, "load attribute itemKey={0}", new Object[]{itemKey});
	getIsChanged(itemKey, params).ifPresent(attribute::setIsChanged);	
	getName(itemKey, params).ifPresent(name->{
	    attribute.setName(name);	    
	    if ("equipmentList".equalsIgnoreCase(name)){
		loadEquipments(itemKey, params, attribute.getContent());
	    }
	});
	getAtrStatus(itemKey, params).ifPresent(attribute::setStatus);
	getAtrValue(itemKey, params).ifPresent(v -> attribute.getContent().add(v));	
	return attribute;
    }
    
    private void loadEquipments(final String prefix, Map<String, String> params, List<Object> content) {
        final String indexKey = prefix + "_" + Params.EQUIPMENT;
	int listsize = getListSize(indexKey, params);
	if (listsize == 0) return;
	content.addAll(
	    IntStream.rangeClosed(1, listsize)
		.filter(index->getId(indexKey + "_" + index, params).isPresent())
		.mapToObj(index->getEquipment(indexKey + "_" + index, params))
		.filter(Objects::nonNull)
		.collect(Collectors.toList())
	);
	content.removeIf(Objects::isNull);
    }
    
    private EquipmentInfo getEquipment(final String itemKey, Map<String, String> params){
	//Params.LOGGER.log(Level.INFO, "Start load equipment for itemKey={0}", new Object[]{itemKey});	
	EquipmentInfo equipment = new EquipmentInfo();
	getId(itemKey, params).ifPresent(equipment::setId);
	getName(itemKey, params).ifPresent(equipment::setName);
	getType(itemKey, params).ifPresent(equipment::setTypeName);
	getStatus(itemKey, params).ifPresent(equipment::setStatus);	
	getResolution(itemKey, params).ifPresent(equipment::setResolution);
	getOptString(itemKey, Params.DESCRIPTION, params).ifPresent(equipment::setDescription);	
	getOptString(itemKey, Params.CENTRALOFFICE_ID, params).ifPresent(equipment::setCentralOfficeId);
	getOptString(itemKey, Params.CATEGORY, params).ifPresent(equipment::setCategory);
	getOptLong(itemKey, Params.AVAILABLE_CAPACIY, params).ifPresent(equipment::setAvailableCapacity);
	getOptLong(itemKey, Params.EXTRACAPACITY, params).ifPresent(equipment::setExtraCapacity);
	getOptBoolean(itemKey, Params.HAS_PROJECT_FIBERLINK, params).ifPresent(equipment::setHasProjectFiberLink);	
	getDateParam(itemKey, Params.COMMISSIONING_DATE, params).ifPresent(equipment::setCommissioningDate);
	//Params.LOGGER.log(Level.INFO, "Start load equipment attributes ...");
	getAttributes(itemKey, params).ifPresent(equipment::setAttributes);
	//Params.LOGGER.log(Level.INFO, "Finish load equipment itemKey={0}", new Object[]{itemKey});
	return equipment;	
    }
    
    private Comments getComments(final String prefix, Map<String, String> params) {
        Comments comments = new Comments();
        final String indexKey = prefix + "_" + Params.COMMENT;
	int listsize = getListSize(indexKey, params);
	//Params.LOGGER.log(Level.INFO, "load comments {0} listsize={1}", new Object[]{indexKey, listsize});
	if (listsize == 0) return null;
	comments.getAny().addAll(
	    IntStream.rangeClosed(1, listsize)
		.mapToObj(i -> getComment(indexKey + "_" + i, params))
		.collect(Collectors.toList())
	);
        return comments;
    }

    private Comment getComment(final String itemKey, Map<String, String> params){
	//Params.LOGGER.log(Level.INFO, "load comment itemKey={0}", new Object[]{itemKey});
	Comment comment = new Comment();
	getCommenter(itemKey, params).ifPresent(comment::setCommenter);
	getText(itemKey, params).ifPresent(comment::setText);
	getType(itemKey, params).ifPresent(comment::setType);
	getDate(itemKey, params).ifPresent(comment::setDate);
	return comment;
    }
    
    private int getListSize(final String key, Map<String, String> params){
	int listSize = 0;	
	Optional<String> value = getOptString(key, Params.LISTSIZE, params);
	if (value.isPresent()){
	    listSize = Integer.valueOf(value.get());
	} 
	//Params.LOGGER.log(Level.INFO, "ListSize {0}={1}", new Object[]{key + "_" + Params.LISTSIZE, listSize});
	return listSize;
    }

    private String getOrderState(Map<String, String> params) {
        return params.get(Params.ORDER_STATE);
    }

    private String getOrderId(Map<String, String> params) {
        return params.get(Params.ORDER_ID);
    }
    
    private String getOrderOMSId(Map<String, String> params) {
        if (params.get(Params.ORDER_OMS_ID) != null){
	    return params.get(Params.ORDER_OMS_ID);
	}
	return params.get(Params.OMS_ID);
    }
	
    private Optional<BigInteger> getAttachmentType(final String key, Map<String, String> params) {
        Optional<String> value = getOptString(key, Params.TYPE, params);
        return value.map(v -> new BigInteger(v));
    }

    private Optional<String> getPartyRole(String key, Map<String, String> params) {
        return getOptString(key, Params.ROLE, params);
    }
    
    private Optional<String> getId(String key, Map<String, String> params) {
        return getOptString(key, Params.ID, params);
    }

    private Optional<String> getOrderItemState(String key, Map<String, String> params) {
        return getOptString(key, Params.STATE, params);        
    }

    private Optional<String> getOrderItemAction(String key, Map<String, String> params) {
        return getOptString(key, Params.ACTION, params);        
    }

    private Optional<String> getOrderItemInstanceId(String key, Map<String, String> params) {
        return getOptString(key, Params.INSTANCE_ID, params);
    }
	
    private Optional<String> getOrderItemAppointmentId(String key, Map<String, String> params) {
        return getOptString(key, Params.INSTANCE_ID, params);
    }
    
    private Optional<String> getIsInheritable(String key, Map<String, String> params) {
        return getOptString(key, Params.ISINHERITABLE, params);
    }

    private Optional<String> getName(String key, Map<String, String> params) {
        return getOptString(key, Params.NAME, params);
    }

    private Optional<String> getText(String key, Map<String, String> params) {        
	return getOptString(key, Params.TEXT, params);
    }    
    
    private Optional<String> getStatus(String key, Map<String, String> params) {
        return getOptString(key, Params.STATUS, params);
    }

    private Optional<String> getType(String key, Map<String, String> params) {
        return getOptString(key, Params.TYPE, params);
    }

    private Optional<String> getCommenter(String key, Map<String, String> params) {
        return getOptString(key, Params.COMMENTER, params);
    }

    private Optional<String> getAuthor(String key, Map<String, String> params) {
        return getOptString(key, Params.AUTHOR, params);
    }

    private Optional<String> getFileExtension(String key, Map<String, String> params) {
        return getOptString(key, Params.FILEEXTENSION, params);
    }

    private Optional<String> getFileName(String key, Map<String, String> params) {
        return getOptString(key, Params.FILENAME, params);
    }

    private Optional<String> getHeader(String key, Map<String, String> params) {
        return getOptString(key, Params.HEADER, params);
    }

    private Optional<String> getRegister(String key, Map<String, String> params) {
        return getOptString(key, Params.REGISTER, params);
    }
	
    private Optional<String> getURL(String key, Map<String, String> params) {
        return getOptString(key, Params.URL, params);
    }

    private Optional<XMLGregorianCalendar> getCreationDate(String key, Map<String, String> params) {
        return getDateParam(key, Params.CREATIONDATE, params);
    }
	
    private Optional<XMLGregorianCalendar> getDate(String key, Map<String, String> params) {
        return getDateParam(key, Params.DATE, params);
    }

    private Optional<XMLGregorianCalendar> getCompletionDate(String key, Map<String, String> params) {
        return getDateParam(key, Params.COMPLETION_DATE, params);
    }

    private Optional<XMLGregorianCalendar> getStartDate(String key, Map<String, String> params) {
        return getDateParam(key, Params.START_DATE, params);
    }

    private Optional<XMLGregorianCalendar> getTimestamp(String key, Map<String, String> params) {
        return getDateParam(key, Params.TIMES_TAMP, params);
    }
    
    private Optional<XMLGregorianCalendar> getDateParam(final String prefix, String key, Map<String, String> params) {
        Optional<String> value = getOptString(prefix, key, params);        
        return value.map(v->Utils.stringToXmlDateTime(v));	
    }
    
    private Optional<AttributeStatus> getAtrStatus(final String key, Map<String, String> params) {
        Optional<String> value = getOptString(key, Params.STATUS, params);
	return value.map(v->AttributeStatus.fromValue(v.toUpperCase()));	
    }

    private Optional<CapabilityResolution> getResolution(final String key, Map<String, String> params) {
        Optional<String> value = getOptString(key, Params.RESOLUTION, params);
	return value.map(v->CapabilityResolution.fromValue(v.toUpperCase()));	    
    }
    
    private Optional<AttributeRestriction> getAttributeRestriction(final String key, Map<String, String> params){
	Optional<String> value = getOptString(key, Params.RESTRICTION, params);
        return value.map(v->AttributeRestriction.fromValue(v.toLowerCase()));
    }
    
    private Optional<Boolean> getIsChanged(final String key, Map<String, String> params) {
        Optional<String> value = getOptString(key, Params.ISCHANGED, params);
        return value.map(v->Boolean.valueOf(v));
    }

    private Optional<Boolean> getIsUpdated(final String key, Map<String, String> params) {
        Optional<String> value = getOptString(key, Params.ISUPDATE, params);
        return value.map(v->Boolean.valueOf(v));
    }
	
    private Optional<String> getAtrValue(final String key, Map<String, String> params) {
	return getOptString(key, Params.VALUE, params);
    }
    
    /**
     * Возвращает Optional<String> из списка params по ключу: prefix_key
     *
     * @param prefix
     * @param key
     * @param params
     * @return
     */
    private Optional<String> getOptString(final String prefix, final String key, Map<String, String> params) {
        if (params.containsKey(prefix + "_" + key)){
	    return Optional.of(params.get(prefix + "_" + key).replaceAll("'", ""));
	}
	return Optional.empty();
    }

    private Optional<Long> getOptLong(final String prefix, final String key, Map<String, String> params) {
        if (params.containsKey(prefix + "_" + key)){
	    return Optional.of(Long.valueOf(params.get(prefix + "_" + key).replaceAll("'", "")));
	}
	return Optional.empty();
    }
    
    private Optional<Boolean> getOptBoolean(final String prefix, final String key, Map<String, String> params) {
        if (params.containsKey(prefix + "_" + key)){
	    return Optional.of(Boolean.valueOf(params.get(prefix + "_" + key).replaceAll("'", "")));
	}
	return Optional.empty();
    }
    
    public Map<String, String> respParamsToMap(Response ilResponse) {	
        Response.ResponseParameters responseParams = ilResponse.getResponseParameters();
	Map<String, String> params = responseParams.getParameter()
		.stream()
		.map(p -> {
		    //Params.LOGGER.log(Level.INFO, "Input param: {0} = {1}", new Object[]{p.getName(), p.getValue()});
		    return p;
		})
		.collect(Collectors.toMap(Parameter::getName, Parameter::getValue));
	params.put(Params.REQUEST_ID, String.valueOf(ilResponse.getResponseHeader().getRequestId()));
	return params;
    }
}
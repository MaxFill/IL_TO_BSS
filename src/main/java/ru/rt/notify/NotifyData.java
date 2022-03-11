package ru.rt.notify;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import ru.rt.dict.Params;
import ru.rt.oms.OrderStatusNotification;
import ru.rt.utils.Utils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotifyData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String url;
    private String notifyId;    
    private String errMsg;
    private String status;
    private Date dateCreate;
    private Date dateSend;
    private String omsId;
    private String orderId;
    private String serviceUID;
    
    @JsonIgnore
    private String xmlData;
    
    @JsonIgnore
    private OrderStatusNotification notification;
    
    public NotifyData() {
    }

    public String getUrl() {
	return url;
    }
    public void setUrl(String url) {
	this.url = url;
    }

    public String getNotifyId() {
	return notifyId;
    }
    public void setNotifyId(String notifyId) {
	this.notifyId = notifyId;
    }

    public String getErrMsg() {
	return errMsg;
    }
    public void setErrMsg(String errMsg) {
	this.errMsg = errMsg;
    }

    public String getStatus() {
	return status;
    }
    public void setStatus(String status) {
	this.status = status;
    }

    public String getServiceUID() {
	return serviceUID;
    }
    public void setServiceUID(String serviceUID) {
	this.serviceUID = serviceUID;
    }
    
    public Date getDateCreate() {
	return dateCreate;
    }
    public void setDateCreate(Date dateCreate) {
	this.dateCreate = dateCreate;
    }

    public Date getDateSend() {
	return dateSend;
    }
    public void setDateSend(Date dateSend) {
	this.dateSend = dateSend;
    }

    public String getOmsId() {
	return omsId;
    }
    public void setOmsId(String omsId) {
	this.omsId = omsId;
    }

    public String getOrderId() {
	return orderId;
    }
    public void setOrderId(String orderId) {
	this.orderId = orderId;
    }  
    
    @JsonIgnore
    public String getXmlData() {
	return xmlData;
    }
    public void setXmlData(String xmlData) {
	this.xmlData = xmlData;
    }
    
    @JsonIgnore
    public OrderStatusNotification getNotification() {
	return notification;
    }
    public void setNotification(OrderStatusNotification notification) {
	this.notification = notification;
    }    
    
    @JsonIgnore
    public boolean isLock(){
	return status.equals(Params.STATUS_COMPLETED);
    }
    
    @JsonIgnore
    public String shortErr(){
	return StringUtils.abbreviate(errMsg, 60);
    }
    
    @JsonIgnore
    public String shortXml(){
	return StringUtils.abbreviate(xmlData, 60);
    }    
    
    @JsonIgnore
    public String getDuration(){		
	return Utils.getDifferenceTime(dateCreate, dateSend);
    }
    
    @JsonIgnore
    public boolean isErrEmpty(){
	return StringUtils.isBlank(errMsg);
    }
    
    @JsonIgnore
    public boolean isXmlEmpty(){
	return StringUtils.isBlank(xmlData);
    }

    @JsonIgnore
    public String getIcon() {
	String icon;
	switch (status){
	    case "":{
		icon = "blank-20";
		break;
	    }
	    case Params.STATUS_COMPLETED:{
		icon = "done-16";
		break;
	    }
	    case Params.STATUS_REJECTED:{
		icon = "importance-20";
		break;
	    }
	    default:{
		icon = "blank-20";
	    }
	}
	return icon;
    }
        
    @Override
    public int hashCode() {
	int hash = 7;
	hash = 37 * hash + Objects.hashCode(this.notifyId);
	hash = 37 * hash + Objects.hashCode(this.omsId);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final NotifyData other = (NotifyData) obj;
	if (!Objects.equals(this.notifyId, other.notifyId)) {
	    return false;
	}
	if (!Objects.equals(this.omsId, other.omsId)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "NotifyData{" + "notifyId=" + notifyId + ", omsId=" + omsId + '}';
    }
   
}

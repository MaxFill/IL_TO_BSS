package ru.rt.notify;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.StringUtils;
import ru.rt.utils.Utils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotifyHelper implements Serializable{  
    private static final long serialVersionUID = 1L;
    
    private final String helperId;
    private final String bssUrl;
    private final String omsId;
    private final String orderId;
    private final String serviceUID;
	
    private final Set<String> errors = new HashSet<>();
    private final AtomicBoolean isErrBlock = new AtomicBoolean(false);	//признак что обработчик временно заблокирован из-за ошибок
    private final AtomicBoolean isLockDown = new AtomicBoolean(false);	//признак что обработчик остановлен до вмешательства админа
    private final AtomicInteger countError = new AtomicInteger();	//счётчик попыток до временной блокировки
    private final AtomicInteger countReRun = new AtomicInteger();	//счётчик попыток до перезапуска временно заблокированного обработчика 
    private final AtomicInteger countLockDown = new AtomicInteger();	//счётчик финальной блокировки обрабочика
    
    private Date dateRun;
    private Date dateStop;
    private String status;
    private String icon;
    
    private boolean isWork = false;	//признак что обработчик работает
    
    public NotifyHelper(String bssUrl, String omsId, String orderId, String serviceUID) {
	this.bssUrl = bssUrl;
	this.omsId = omsId;
	this.orderId = orderId;
	this.helperId = Utils.generateUID();
	this.serviceUID = serviceUID;
    }

    public AtomicBoolean isLockDown(){
	return isLockDown;
    }
    public void setIsLockDown(boolean value){
	isLockDown.getAndSet(value);
	status = DictStatuses.STATUS_FINAL_LOCK;
	icon = "stop-20";
    }

    public String getServiceUID() {
	return serviceUID;
    }
        
    public String getBssUrl() {
	return bssUrl;
    }

    public String getOmsId(){
	return omsId;
    }

    public String getOrderId() {
	return orderId;
    }
      
    public Set<String> getErrors() {
	return errors;
    }
   
    public String getIcon() {	
	return icon;
    }
    
    public boolean isWork() {	
	return isWork;
    }   

    public String getHelperId() {
	return helperId;
    }
	
    public Date getDateRun() {
	return dateRun;
    }

    public Date getDateStop() {
	return dateStop;
    }
    
    public String getStatus(){
	return status;
    }
    public void setStatus(String status) {	
	this.status = status;
    }
    
    /* *** methods *** */
    
    @JsonIgnore
    public void setRun(){
	isWork = true;
	dateRun = new Date();
	status = DictStatuses.STATUS_WORKS;
	icon = "service-20";
    }
    
    @JsonIgnore
    public void setStop(){
	isWork = false;
	dateStop = new Date();	
    }
    
    @JsonIgnore
    public boolean isStop(){
	return !status.equals(DictStatuses.STATUS_WORKS) && !status.equals(DictStatuses.STATUS_RESET);
    }
    
    @JsonIgnore
    public boolean isFinalLock(){
	return isLockDown.get();
    }
    
    @JsonIgnore
    public Boolean isErrBlock() {
	return isErrBlock.get();
    }
    public void setIsErrBlock(){
	isErrBlock.getAndSet(true);
	status = DictStatuses.STATUS_TEMP_LOCK;
	icon = "importance-20";
    }
    
    @JsonIgnore
    public void setSuccessfullyStatus(){
	status = DictStatuses.STATUS_SUCCESSFULLY;
	icon = "done-16";
    }
    
    @JsonIgnore
    public boolean isSuccessfully(){
	return DictStatuses.STATUS_SUCCESSFULLY.equals(status);
    }
    
    @JsonIgnore
    public int addCountLockDown(){
	return countLockDown.incrementAndGet();
    }
   
    @JsonIgnore
    public int addCountReRun(){
	return countReRun.incrementAndGet();
    }
    
    @JsonIgnore
    public void clearCountLockDown(){
	countLockDown.getAndSet(0);
    }      
	
    @JsonIgnore
    public void clearErrors(){
	errors.clear();
	countError.set(0);
	countReRun.set(0);
	isErrBlock.getAndSet(false);	
    }
    
    @JsonIgnore
    public void reset(){
	isLockDown.set(false);
	status = DictStatuses.STATUS_RESET;
	icon = "refresh-16";
	clearErrors();
    }
    
    @JsonIgnore
    public Integer addErrorCount(){
	return countError.incrementAndGet();
    }

    @JsonIgnore
    public int getCountLockDown(){
	return countLockDown.get();
    }    
    
    @JsonIgnore
    public int getCountErr(){
	return countError.get();
    }     

    @JsonIgnore
    public int getCountReRun(){
	return countReRun.get();
    }     
    
    @JsonIgnore
    public String getDuration(){
	return Utils.getDifferenceTime(dateRun, dateStop);
    }        
       
    @JsonIgnore
    public String shortErr(){
	return StringUtils.abbreviate(getErrMsg(), 60);
    }
	
    @JsonIgnore
    public String shortUrl(){
	return StringUtils.abbreviate(bssUrl, 50);
    }
	
    @JsonIgnore
    public String fullErr(){
	return getErrMsg();
    }
    
    @JsonIgnore
    public boolean isErrEmpty(){
	return errors.isEmpty();
    }
	
    @JsonIgnore
    public String getKey(){
	if (StringUtils.isBlank(omsId)){
	    return bssUrl;
	} else {
	    return omsId + orderId + bssUrl;
	}
    }
    
    @JsonIgnore
    private String getErrMsg(){
	return StringUtils.join(errors, ", ");
    }
        
    @Override
    public int hashCode() {
	int hash = 7;
	hash = 83 * hash + Objects.hashCode(this.bssUrl);
	hash = 83 * hash + Objects.hashCode(this.omsId);
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
	final NotifyHelper other = (NotifyHelper) obj;
	if (!Objects.equals(this.omsId, other.omsId)) {
	    return false;
	}
	if (!Objects.equals(this.bssUrl, other.bssUrl)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "NotifyHelper{" + "bssUrl=" + bssUrl + ", omsId=" + omsId + '}';
    }
    
}
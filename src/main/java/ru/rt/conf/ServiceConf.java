package ru.rt.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Конфигурационные параметры сервиса нотификаций
 * @author Maksim.Filatov
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceConf implements Serializable{  
    private static final long serialVersionUID = 1L;
    
    private Date dateStart = null;
    private Date dateStop = null;
    private Long interval = null;		//интервал перезапуска таймера
    private Boolean autoDelQueue = true;
    private Integer countErrForLock;		//количество неудачных попыток запуска (ошибок) после чего очередь блокируется
    private Integer countAttemptsForRestart;	    
    private Integer countAttemptsForLockdown;	    
    private Integer timeOutRequest;
    private Integer timeOutConnect;
    private String serviceStatus;
    private final String serviceGuid;
    private final ConcurrentHashMap<String, String> notifySending;

    public ServiceConf(String serviceGuid, ConcurrentHashMap<String, String> notifySending) {
	this.notifySending = notifySending;
	this.serviceGuid = serviceGuid;
    }

    public String getServiceGuid() {
	return serviceGuid;
    }
    
    public Integer getCountSending(){
	return notifySending.size();
    }

    public String getServiceStatus() {
	return serviceStatus;
    }
    public void setServiceStatus(String serviceStatus) {
	this.serviceStatus = serviceStatus;
    }        

    public Integer getCountErrForLock() {
	return countErrForLock;
    }
    public void setCountErrForLock(Integer countErrForLock) {
	this.countErrForLock = countErrForLock;
    }

    public Integer getCountAttemptsForRestart() {
	return countAttemptsForRestart;
    }
    public void setCountAttemptsForRestart(Integer countAttemptsForRestart) {
	this.countAttemptsForRestart = countAttemptsForRestart;
    }

    public Integer getCountAttemptsForLockdown() {
	return countAttemptsForLockdown;
    }
    public void setCountAttemptsForLockdown(Integer countAttemptsForLockdown) {
	this.countAttemptsForLockdown = countAttemptsForLockdown;
    }

    public Integer getTimeOutRequest() {
	return timeOutRequest;
    }
    public void setTimeOutRequest(Integer timeOutRequest) {
	this.timeOutRequest = timeOutRequest;
    }

    public Integer getTimeOutConnect() {
	return timeOutConnect;
    }
    public void setTimeOutConnect(Integer timeOutConnect) {
	this.timeOutConnect = timeOutConnect;
    }
      
    public Boolean getAutoDelQueue() {
	return autoDelQueue;
    }
    public void setAutoDelQueue(Boolean autoDelQueue) {
	this.autoDelQueue = autoDelQueue;
    }
    
    public Date getDateStart() {
	return dateStart;
    }
    public void setDateStart(Date dateStart) {
	this.dateStart = dateStart;
    }

    public Date getDateStop() {
	return dateStop;
    }
    public void setDateStop(Date dateStop) {
	this.dateStop = dateStop;
    }

    public Long getInterval() {
	return interval;
    }
    public void setInterval(Long interval) {
	this.interval = interval;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 11 * hash + Objects.hashCode(this.serviceGuid);
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
	final ServiceConf other = (ServiceConf) obj;
	if (!Objects.equals(this.serviceGuid, other.serviceGuid)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "ServiceConf{" + "serviceGuid=" + serviceGuid + '}';
    }
        
}

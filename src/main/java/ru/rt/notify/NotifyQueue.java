package ru.rt.notify;

import java.util.Objects;

public class NotifyQueue {
    private final String bssUrl;
    private final String omsId;
    private final String orderId;
    
    public NotifyQueue(String bssUrl, String omsId, String orderId) {
	this.bssUrl = bssUrl;
	this.omsId = omsId;
	this.orderId = orderId;
    }

    public String getBssUrl() {
	return bssUrl;
    }

    public String getOmsId() {
	return omsId;
    }

    public String getOrderId() {
	return orderId;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 71 * hash + Objects.hashCode(this.bssUrl);
	hash = 71 * hash + Objects.hashCode(this.omsId);
	hash = 71 * hash + Objects.hashCode(this.orderId);
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
	final NotifyQueue other = (NotifyQueue) obj;
	if (!Objects.equals(this.bssUrl, other.bssUrl)) {
	    return false;
	}
	if (!Objects.equals(this.omsId, other.omsId)) {
	    return false;
	}
	if (!Objects.equals(this.orderId, other.orderId)) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "NotifyQueue{" + "omsId=" + omsId + ", orderId=" + orderId + '}';
    }        
        
}

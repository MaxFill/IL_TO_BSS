package ru.rt.mail;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maksim.Filatov
 */
public class MailMsg {
    private String sender;
    private String recipients;
    private String copyes;
    private String content;
    private String subject;
    private String repeat; //repeat send in seconds
    private Map<String,String> attachments;
    private Date dateSend;
    
    public MailMsg() {
    }
    
    /* gets & sets */

    public Date getDateSend() {
	return dateSend;
    }
    public void setDateSend(Date dateSend) {
	this.dateSend = dateSend;
    }    
    
    public Map<String, String> getAttachments() {
	if (attachments == null){
	    attachments = new HashMap<>();
	}
	return attachments;
    }
    public void setAttachments(Map<String, String> attachments) {
	this.attachments = attachments;
    }    
    
    public String getSender() {
	return sender;
    }
    public void setSender(String sender) {
	this.sender = sender;
    }

    public String getRecipients() {
	return recipients;
    }
    public void setRecipients(String recipients) {
	this.recipients = recipients;
    }

    public String getCopyes() {
	return copyes;
    }
    public void setCopyes(String copyes) {
	this.copyes = copyes;
    }

    public String getContent() {
	return content;
    }
    public void setContent(String content) {
	this.content = content;
    }

    public String getSubject() {
	return subject;
    }
    public void setSubject(String subject) {
	this.subject = subject;
    }
        
}

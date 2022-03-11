package ru.rt.mail;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import ru.rt.conf.Conf;
import static ru.rt.dict.Params.LOGGER;

/**
 * Отправка e-mail сообщений об ошибках на почту 
 * @author Maksim.Filatov
 */
@Stateless
public class MailAlert { 
    @EJB private Conf conf;
     
    @Asynchronous
    public void sendMailAlert(Set<String> messages) {
	//LOGGER.log(Level.INFO, "------------ Start send mail alert -------------");
	String content = String.join(", \r\n", messages); 
	MailMsg mailMsg = new MailMsg();
	mailMsg.setContent(content);
	mailMsg.setRecipients("Maksim.Filatov@rt.ru");	
	mailMsg.setSender("fsom@rt.ru");
	mailMsg.setSubject("В ходе отправки нотификаций в BSS возникли ошибки!");
	mailMsg.setAttachments(new HashMap<>());	
	try {
	    sendMultiMessage(mailMsg);
	} catch (MessagingException | UnsupportedEncodingException ex) {
	    LOGGER.log(Level.SEVERE, "При отправки e-mail возникла ошибка: {0}", new Object[]{ex.getMessage()});
	}	
    }      
     
    /**
     * Формирует соединение с почтовым сервером для отправки сообщений
     * @param settings
     */
    private Session getMailSessionSender() {  
	Authenticator auth = new MailAuth(conf.getMailUser(), conf.getMailPassword());
	LOGGER.log(Level.SEVERE, "Mail server connect for {0} {1}", new Object[]{conf.getMailUser(), conf.getMailPassword()});
	Properties props = System.getProperties();
	props.put("mail.transport.protocol", "smtp");	    	    
	props.put("mail.smtp.port", conf.getMailPort());	    
	props.put("mail.smtp.host", conf.getMailHostName());
	props.put("mail.smtp.auth", "true");
	props.put("mail.mime.charset", conf.getMailEncoding());
	props.put("mail.smtp.starttls.enable", "false");

	if (conf.getMailUseSSL()) {
	    LOGGER.log(Level.SEVERE, "Для передачи e-mail будет использоваться SSL!");
	    props.put("mail.smtp.ssl.enable", "true");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	} else {		
	    props.put("mail.smtp.ssl.enable", "false");
	}
	return Session.getInstance(props, auth);
    }
    
    private void sendMultiMessage(MailMsg mailMsg) throws MessagingException, UnsupportedEncodingException {	
	final String encoding = conf.getMailEncoding();
	Session session = getMailSessionSender();
	MimeMessage msg = new MimeMessage(session);
	msg.setFrom(new InternetAddress(mailMsg.getSender()));	
	final String recipients = mailMsg.getRecipients();
	if (recipients == null || recipients.isEmpty()){
	    throw new MessagingException("Recipient is empty!");
	}
	msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
	final String copyes = mailMsg.getCopyes();
	if (copyes != null && !copyes.isEmpty()){
	    msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(copyes));
	}
	msg.setSubject(mailMsg.getSubject(), encoding);
	BodyPart messageBodyPart = new MimeBodyPart();
	messageBodyPart.setContent(mailMsg.getContent(), "text/html; charset=" + encoding + "");	
	Multipart multipart = new MimeMultipart();
	multipart.addBodyPart(messageBodyPart);	
	for(Map.Entry<String, String> attachment : mailMsg.getAttachments().entrySet()){
	    MimeBodyPart attachmentBodyPart = new MimeBodyPart();
	    DataSource source = new FileDataSource(attachment.getValue());
	    attachmentBodyPart.setDataHandler(new DataHandler(source));
	    attachmentBodyPart.setFileName(MimeUtility.encodeText(attachment.getKey()));
	    multipart.addBodyPart(attachmentBodyPart);
	}
	msg.setContent(multipart);
	Transport.send(msg);
    }
}
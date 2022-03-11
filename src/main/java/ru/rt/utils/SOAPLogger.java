package ru.rt.utils;

import com.comptel.soa._2011._02.instantlink.Response;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import ru.rt.dict.Params;
import static ru.rt.dict.Params.LOGGER;
import ru.rt.oms.NotificationResponse;
import ru.rt.oms.OrderStatusNotification;

/**
 * Запись в файл содержимого асинхронной нотификации 
 * @author Maksim.Filatov
 */
@Stateless
public class SOAPLogger {
    private final static String LOG_DIR = "/opt/wildfly/standalone/log/comb2b/";
    //https://ihelp.rt.ru/browse/FSOM-26
    public void toServerLog(String orderId, String omsId, String notifyId, String bssURL, String eventCode, String msg){	
	LOGGER.log(Level.INFO, "FSOM->COM {0} {1} {2} {3} {4} {5}", new Object[]{orderId, omsId, notifyId, bssURL, eventCode, msg});
    }
    
    @Asynchronous
    public void notificationToLog(OrderStatusNotification notification, final String notifId, String dateEvent){
	try {	    
	    JAXBContext jaxbContext = JAXBContext.newInstance(OrderStatusNotification.class);
	    Marshaller marshaller = jaxbContext.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    JAXBElement<OrderStatusNotification> jaxbElement = new JAXBElement<>(new QName("", "OrderStatusNotification"), OrderStatusNotification.class, notification);
	    StringWriter sw = new StringWriter();
	    marshaller.marshal(jaxbElement, sw);
	    saveLogFile("request", notification.getOrder().getOrderId(), notifId, sw, dateEvent);
	} catch (JAXBException | IOException ex) {
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	}
    }
    
    @Asynchronous
    public void responseToLog(NotificationResponse response, String orderId, String notifyId, String dateEvent){
	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance(NotificationResponse.class);
	    Marshaller marshaller = jaxbContext.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    JAXBElement<NotificationResponse> jaxbElement = new JAXBElement<>(new QName("", "NotificationResponse"), NotificationResponse.class, response);
	    StringWriter sw = new StringWriter();
	    marshaller.marshal(jaxbElement, sw);	    
	    saveLogFile("resp", orderId, notifyId, sw, dateEvent);
	} catch (JAXBException | IOException ex) {
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	}
    }
    
    @Asynchronous
    public void requestToLog(Response request, final String orderId, final String notifyId, String dateEvent){
	try {
	    Class source = request.getClass();
	    String name = source.getSimpleName();
	    JAXBContext jaxbContext = JAXBContext.newInstance(source);
	    Marshaller marshaller = jaxbContext.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    JAXBElement<Response> jaxbElement = new JAXBElement<>(new QName("soa.comptel.com", name), source, request);
	    StringWriter sw = new StringWriter();
	    marshaller.marshal(jaxbElement, sw);
	    saveLogFile("request", orderId, notifyId, sw, dateEvent);
	} catch (JAXBException | IOException ex) {
	    Params.LOGGER.log(Level.SEVERE, null, ex.getMessage());
	}
    }
    
    /* *** privates *** */    
    
    private void saveLogFile(final String ext, final String orderId, final String notifyId, StringWriter sw, String dateEvent) throws UnsupportedEncodingException, IOException {
        File dir = new File(LOG_DIR);
        if (!dir.exists()){
            dir.mkdirs();
        }
        StringBuilder fileName = new StringBuilder();
        fileName.append(LOG_DIR).append(orderId).append("_").append(notifyId).append(".").append(ext) ;        
        StringBuilder content = new StringBuilder();
	content.append(dateEvent)
	    .append(" ").append(Params.LOGGER_NAME)
	    .append(" ").append(orderId)
	    .append(" ").append(notifyId)
	    .append(" ").append(sw.toString());	
	Files.write(Paths.get(fileName.toString()), content.toString().getBytes("utf-8"), 
	    StandardOpenOption.CREATE, 
	    StandardOpenOption.TRUNCATE_EXISTING);	
    }    
}
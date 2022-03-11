package ru.rt.notify;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import ru.rt.conf.Conf;
import static ru.rt.dict.Params.LOGGER;
import ru.rt.oms.OMSOrderNotificationWebService;
import ru.rt.utils.SOAPLogger;

@Stateless
@TransactionAttribute(value= TransactionAttributeType.NEVER) 
public class NotifyWorker {     
	    
    @EJB private Conf conf;
    @EJB private SOAPLogger logger;        
    
    @Asynchronous
    public void doWork(OMSOrderNotificationWebService bssService, Set<NotifyQueue> notifyQueues, ConcurrentHashMap<String, NotifyHelper> helpers, ConcurrentHashMap<String, String> notifySending, boolean isAutoDelQueue) {
	try {
	    notifyQueues
	    .parallelStream()
	    .forEach(queue ->{
		NotifySender notifySender = new NotifySender(bssService, conf, logger);
		final NotifyHelper notifyHelper = notifySender.getNotifyHelper(queue, helpers);
		final String bssUrl = notifyHelper.getBssUrl();
		final String omsId = notifyHelper.getOmsId();
		try {
		    if (notifyHelper.isLockDown().get()) { 
			LOGGER.log(Level.INFO, "{0} {1} Обработчик остановлен и заблокирован из-за множественных ошибок!", new Object[]{bssUrl, omsId});
		    } else {
			//проверяем, работает ли данный обработчик
			if (notifyHelper.isWork()) { //обработчик работает
			    LOGGER.log(Level.INFO, "{0} {1} Обработчик уже выполняется!", new Object[]{bssUrl, omsId});
			} else { //если обработчик не работает
			    if (notifyHelper.isErrBlock()){ //если он заблокирован из-за ошибок
				int count = notifyHelper.addCountReRun(); //увеличиваем счётчик попыток до перезапуска
				if (count > conf.getCountAttemptsForRestart()){ //пора перезапустить! будет перезапуск обработчика
				    notifyHelper.clearErrors();
				    notifyHelper.addCountLockDown(); //увеличиваем счётчик финальной блокировки, после которого обработчик будет заблокирован окончательно 
				    LOGGER.log(Level.INFO, "{0} {1} Обработчик будет перезапущен!", new Object[]{bssUrl, omsId});
				    notifySender.doAsyncWork(notifyHelper, helpers, notifySending);
				} else {
				    int countReRun = conf.getCountAttemptsForRestart() - notifyHelper.getCountReRun();
				    LOGGER.log(Level.INFO, "{0} {1} Обработчик временно заблокирован! Циклов до перезапуска={2}", new Object[]{bssUrl, omsId, countReRun});
				}
			    } else {	//обработчик не блокирован и его можно запустить				
				notifySender.doAsyncWork(notifyHelper, helpers, notifySending);
			    }
			}
			
			if (isAutoDelQueue && DictStatuses.STATUS_SUCCESSFULLY.equals(notifyHelper.getStatus())){    
			    helpers.remove(notifyHelper.getKey());
			}
		    }
		} catch(Exception ex){
		    LOGGER.log(Level.SEVERE, "{0} {1} ERROR occurred: {2}", new Object[]{bssUrl, omsId, getShortMsg(ex.getMessage())});
		}
	    });
	    //LOGGER.log(Level.SEVERE, "----------------------- завершено --------------------------- " );
	} catch(Exception ex){
	    LOGGER.log(Level.SEVERE, "ERROR in doTimerWork: {0}", getShortMsg(ex.getMessage()));
	}
    }    
    
    /* *** privates ***/   
    
    private String getShortMsg(String longMsg){
	String shortMsg = "";
	if (longMsg != null){
	    shortMsg = longMsg.substring(0, Math.min(conf.getErrMinLenght(), longMsg.length()));
	}
	return shortMsg;
    }

}

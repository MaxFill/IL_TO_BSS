package ru.rt.notify;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import org.apache.commons.lang3.StringUtils;
import ru.rt.conf.Conf;
import ru.rt.conf.ServiceConf;
import ru.rt.dict.Params;
import static ru.rt.dict.Params.LOGGER;
import static ru.rt.notify.DictStatuses.TIMER_STATUS_RUNNING;
import static ru.rt.notify.DictStatuses.TIMER_STATUS_STOPED;
import ru.rt.oms.OMSOrderNotificationWebService;

@Startup
@Singleton
public class NotifyTimer {    
    @EJB private Conf conf;
    @EJB private NotifyWorker notifyWorker;
    @EJB private NotifyPersist notifyPersist;    
    @EJB private NotifyUtils notifyUtils;
    
    @Resource TimerService timerService;
    
    private Timer timer;
    private ServiceConf serviceConf;
    private ConcurrentHashMap<String, String> notifySending;    
    
    private final ConcurrentHashMap<String, NotifyHelper> workers = new ConcurrentHashMap<>();   
    
    @PostConstruct
    private void init(){
	try {
	    notifySending = new ConcurrentHashMap<>();
	    serviceConf = new ServiceConf(conf.getServiceGuid(), notifySending);
	    serviceConf.setInterval(conf.getTimerIntervalLaunch());
	    serviceConf.setCountAttemptsForRestart(conf.getCountAttemptsForRestart());
	    serviceConf.setCountAttemptsForLockdown(conf.getCountAttemptsForLockdown());
	    serviceConf.setCountErrForLock(conf.getCountErrForLock());
	    serviceConf.setTimeOutConnect(conf.getTimeOutConnect());
	    serviceConf.setTimeOutRequest(conf.getTimeOutRequest());    
	    startTimer();
	} catch (Exception ex) {
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	}
    }
    
    public void startTimer(){	
	LOGGER.log(Level.INFO, "Запуск таймера.... частота повтора = {0} sec.", serviceConf.getInterval() / 1000);
	TimerConfig timerConfig = new TimerConfig();
	timerConfig.setPersistent(false);
	Date startDate = new Date();
	timer = timerService.createIntervalTimer(startDate, serviceConf.getInterval(), timerConfig);	
	serviceConf.setDateStart(startDate);
	serviceConf.setDateStop(null);
	serviceConf.setServiceStatus(TIMER_STATUS_RUNNING);
	notifySending.clear();
    }
    
    public void stopTimer(){
	timer.cancel();	
	serviceConf.setDateStop(new Date());
	serviceConf.setDateStart(null);
	serviceConf.setServiceStatus(TIMER_STATUS_STOPED);
	LOGGER.log(Level.INFO, "Таймер остановлен!");
    }    
    
    @Timeout
    public void doTimer(Timer timer) {	
	LOGGER.log(Level.INFO, "++++++++++++++++++++++++ Do timer work ++++++++++++++++++++++++++++++++++");	
	//проверка временно заблокированных url - разблокировка или установка фин. блокировки
	workers.entrySet().stream()
	    .filter(entry->{
		NotifyHelper h = entry.getValue();
		return !h.isFinalLock() && h.getOmsId() == null && h.isErrBlock();
	    })
	    .forEach(entry->{
		NotifyHelper helper = entry.getValue();	    
		//увеличиваем счётчик CountReRun до перезапуска и проверяем что можно ли его перезапустить
		if (helper.addCountReRun() > conf.getCountAttemptsForRestart()){
		    //да, достигнуто значение, после которого можно перезапустить очередь
		    //увеличиваем счётчик перезапусков CountLockDown 
		    //и если колво перезапусков достигло максимума, то ставим финальную блокировку на очередь
		    if (helper.addCountLockDown() > conf.getCountAttemptsForLockdown()){ 
			LOGGER.log(Level.INFO, "{0} для URL установлена финальная блокировка!", new Object[]{helper.getBssUrl()});
			helper.setIsLockDown(true); //достигнут предел перезапусков, устанавливаем финальную блокировку
		    } else {
			//колво перезапусков ещё не достигло максимума, поэтому перезапуск разрешён
			LOGGER.log(Level.INFO, "{0} URL будет перезапущен", new Object[]{helper.getBssUrl()});
			helper.clearErrors();	//готов к перезапуску
		    }
		} else {
		    int countReRun = conf.getCountAttemptsForRestart() - helper.getCountReRun();
		    LOGGER.log(Level.INFO, "{0} URL временно заблокирован. Циклов до перезапуска {1}, LockDownd {2} из {3}", new Object[]{helper.getBssUrl(), countReRun, helper.getCountLockDown(), conf.getCountAttemptsForLockdown()});
		}
	    });	    
	doWork();
    }
    
    public void doWork(){
	try {
	    Set<String> bssUrls = notifyPersist.getNotifyURLs();
	    int countOrder = bssUrls.size();
	    if (countOrder == 0) {
		LOGGER.log(Level.INFO, "Сейчас нет нотификаций для отправки. Уже было отправлено {0}", notifySending.size());
		return;
	    }
	    LOGGER.log(Level.INFO, "Найдено {0} url, на которые нужно отправить нотификации", new Object[]{countOrder});
	    bssUrls
		.stream()
		.forEach(url->{
		    Set<NotifyQueue> notifyQueue = notifyPersist.getNotifyQueue(url);
		    if (notifyQueue.isEmpty()){
			LOGGER.log(Level.INFO, "{0} Для сервиса {1} нотификации не найдены", new Object[]{url, conf.getServiceGuid()});						
			return;
		    }
		    LOGGER.log(Level.INFO, "{0} Для сервиса {1} найдено {2} заказа(ов), для которых есть нотификации", new Object[]{url, conf.getServiceGuid(), notifyQueue.size()});
		    NotifyHelper helper = workers.get(url);
		    if (helper != null && (helper.isFinalLock() || helper.isErrBlock())){
			LOGGER.log(Level.INFO, "{0} Отправка нотификаций на [{1}] не будет выполнятся, так как установлена блокировка сервиса!", new Object[]{url});	
			return;
		    }
		    if (helper == null){
			helper = new NotifyHelper(url, null, null, conf.getServiceGuid());
			workers.put(url, helper);
		    }	    
		    Set<String> errors = new HashSet<>();
		    OMSOrderNotificationWebService bssService = notifyUtils.initWebService(url, errors);		    
		    if (bssService != null && errors.isEmpty()) {
			deleteHelper(helper); //поскольку инициализация web сервиса выполнена успешно, то удалём 'пустую' очередь и создаём новую уже с нотификациями			
			notifyWorker.doWork(bssService, notifyQueue, workers, notifySending, getAutoDelQueue());			    
		    } else {
			//при инициализации возникла ошибка
			helper.setRun();
			helper.setStop();
			helper.getErrors().addAll(errors);
			int countForLock = conf.getCountErrForLock() - helper.getCountErr();
			LOGGER.log(Level.INFO, "{0} Временная блокировка URL будет установлена через {1} попытки(ок)!", new Object[]{url, countForLock});
			if (helper.addErrorCount() > conf.getCountErrForLock()) {
			    LOGGER.log(Level.INFO, "{0} Данный URL временно заблокирован из-за повторяющихся ошибок!", new Object[]{url});
			    helper.setIsErrBlock(); 
			}		
		    }
		});	    
	    LOGGER.log(Level.SEVERE, "----------------------- finish --------------------------- " );
	} catch(Exception ex){
	    LOGGER.log(Level.SEVERE, "Timer error: {0}", ex.getMessage());
	}
    }    
    
    public String restartWorker(NotifyHelper helper, Boolean isResetStatus, String serviceGuid){
	String bssURL = helper.getBssUrl();
	String omsId = helper.getOmsId();
	String orderId = helper.getOrderId();
	if (StringUtils.isBlank(bssURL) || StringUtils.isBlank(omsId)){
	    return "Input params incorrect! The omsId and bssUrl parameters must be specified!";
	}
	StringBuilder result = new StringBuilder();
	if (Params.ALL.equalsIgnoreCase(omsId)){
	    workers.entrySet().stream()
		.filter(entry->entry.getKey().contains(bssURL))
		.forEach(entry->result.append(restart(entry.getValue(), isResetStatus, serviceGuid)));
	} else {
	    NotifyHelper notifyHelper = workers.get(omsId + orderId + bssURL);
	    result.append(restart(notifyHelper, isResetStatus, serviceGuid));
	}
	if (result.length() == 0){
	    result.append("Queue not found!");
	}
	return result.toString();
    }
    
    public boolean deleteHelper(NotifyHelper helper){
	boolean result = false;
	if (helper == null) return result;	
	if (!helper.isWork()){	    
	    String key = helper.getKey();	    
	    if (getWorkers().remove(key) != null){
		LOGGER.log(Level.INFO, "{0} {1} Обработчик удалён!", new Object[]{helper.getBssUrl(), helper.getOmsId()});
		result = true;
	    } else {
		LOGGER.log(Level.INFO, "{0} {1} Обработчик не удалён, так как не найден!", new Object[]{helper.getBssUrl(), helper.getOmsId()});
	    }
	}
	return result;
    }
    
    public ConcurrentHashMap<String, NotifyHelper> getWorkers(){
	return workers;
    }

    public String getTimerStatus() {	
	return serviceConf.getServiceStatus();
    }

    public Date getDateStart() {
	return serviceConf.getDateStart();
    }
    public Date getDateStop() {
	return serviceConf.getDateStop();
    }

    public Long getInterval() {
	return serviceConf.getInterval();
    }
    public void setInterval(Long interval) {
	serviceConf.setInterval(interval);
    }

    public Boolean getAutoDelQueue() {
	return serviceConf.getAutoDelQueue();
    }
    public void setAutoDelQueue(Boolean autoDelQueue) {
	serviceConf.setAutoDelQueue(autoDelQueue);
    }    
    
    public ServiceConf getServiceConf(){
	return serviceConf;
    }
    
    /* *** privates *** */  
    
    /**
     * Перезапуск очереди
     * @param notifyHelper
     * @param isResetStatus - признак сброса статуса у нотификаций
     * @param serviceGuid - запись гуида в нотификации, если не равен null
     * @return 
     */
    private String restart(NotifyHelper notifyHelper, Boolean isResetStatus, String serviceGuid){
	LOGGER.log(Level.SEVERE, "Перезапуск обработчика очереди...");
	if (notifyHelper == null) return "queue not found!";
	
	final String orderId = notifyHelper.getOrderId();
	final String bssURL = notifyHelper.getBssUrl();
	final String omsId = notifyHelper.getOmsId();
	final String queue = "Queue " + bssURL + " " +  omsId;			
	
	if (notifyHelper.isWork()){
	    return queue + " cannot be restarted because it is running!";
	}
	
	LOGGER.log(Level.SEVERE, "{0} Обработчик для OrderId={1} OmsId={2} будет перезапущен!", new Object[]{bssURL, omsId, orderId});
	
	//сброс статусов нотификаций
	int countUpdated = 0;
	if (isResetStatus){
	    Set<String> errors = new HashSet<>();
	    countUpdated = notifyPersist.clearNotifiesStatus(bssURL, omsId, errors);
	    if (!errors.isEmpty()){
		return "Internal server error occurred when clearing notification statuses!";
	    }
	}
	if (StringUtils.isNotBlank(serviceGuid)){
	    Set<String> errors = new HashSet<>();
	    notifyPersist.updateServiceGuid(serviceGuid, bssURL, omsId, errors);
	}
	notifyHelper.reset();
	if (notifyPersist.getNotifyQueue(bssURL).isEmpty()){
	    workers.remove(notifyHelper.getKey());
	    LOGGER.log(Level.INFO, "{0} Обработчик удалён, т.к. для него нет нотификаций", new Object[]{bssURL});
	    return "queue delete";
	}
	LOGGER.log(Level.SEVERE, "{0} Обработчик перезапущен!", new Object[]{bssURL});
	return queue + " queue now was restarted! Updated " + countUpdated + " notify. "; 
    }        
      
}
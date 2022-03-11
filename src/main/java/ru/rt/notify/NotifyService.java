package ru.rt.notify;

import com.google.gson.Gson;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.rt.dict.Params;
import static ru.rt.dict.Params.LOGGER;

/**
 * REST API FSOM NOTIFIES
 * @author Maksim.Filatov
 */
@Path("/notifies")
@Stateless
public class NotifyService {
    protected static final Logger LOGGER = Logger.getLogger(Params.LOGGER_NAME);
    
    @EJB private NotifyTimer notifyTimer;          
    @EJB private NotifyPersist notifyPersist;
    @EJB private NotifyUtils notifyUtils;
    
    public NotifyService() {
    }
       
    /**
     * Возвращает список очередей
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getAllQueues(){
	List<NotifyHelper> results = notifyTimer.getWorkers().entrySet().stream().map(v->v.getValue()).collect(Collectors.toList());
	LOGGER.log(Level.INFO, "REST_SERV getAllQueues size={0}", results.size());
	return Response.ok(results).build();
    }
    
    /**
     * Выполняет поиcк нотификации по заданным параметрам
     * @param gsonStr - фильтр (нотификация) в формате JSON
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    @Path("/findNotifies")
    public Response findNotifies(String gsonStr){
	LOGGER.log(Level.INFO, "REST_SERV findNotifies start from gson={0}", gsonStr);
	Gson gson = new Gson();
	NotifyData filter = gson.fromJson(gsonStr, NotifyData.class);
	if (filter == null){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	List<NotifyData> results = notifyPersist.findNotify(filter);
	LOGGER.log(Level.INFO, "REST_SERV findTikets result size={0}", results.size());
	if (results.isEmpty()){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}	
	return Response.ok(results).build();
    }
    
    /**
     * Выполняет удаление очереди
     * @param queueId
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)    
    @DELETE
    @Path("/delQueue")
    public Response delQueue(@QueryParam("queueId") String queueId){
	if (queueId == null){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	NotifyHelper helper = notifyTimer.getWorkers().get(queueId);
	if (helper == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	if (notifyTimer.deleteHelper(helper)){
	    return Response.ok().build();    
	} else {
	    return Response.status(Response.Status.NOT_MODIFIED).build();
	}
    }
    
    /**
     * Выполняет перезапуск обработчика очереди нотификаций
     * @param queueId
     * @param isResetStatus
     * @param serviceGuid
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/restartQueue")
    public Response restartQueue(
	    @QueryParam("queueId") String queueId, 
	    @QueryParam("isResetStatus") Boolean isResetStatus,
	    @QueryParam("serviceGuid") String serviceGuid)
    {
	NotifyHelper helper = notifyTimer.getWorkers().get(queueId);
	if (helper == null){
	    return Response.status(Response.Status.NOT_FOUND).build();
	}
	String result = notifyTimer.restartWorker(helper, isResetStatus, serviceGuid);
	return Response.ok(result).build();
    }
    
    /**
     * Выполняет проверку доступности web сервиса CRM
     * @param bssUrl
     * @return
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/checkAvailabilityURL")
    public Response checkAvailabilityURL(@QueryParam("bssUrl") String bssUrl){
	Set<String> messages = new HashSet<>();
	final URL url = notifyUtils.validateURL(bssUrl, messages);
	if (url != null) {
            messages.add("CRM web service is correct and available!");
        }
	return Response.ok(messages).build();
    }
    
    /**
     * Возвращает текущий статус сервиса
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/serviceStatus")
    public Response getServiceStatus(){	
	return Response.ok(notifyTimer.getTimerStatus()).build();
    }
    
    /**
     * Возвращает текущую конфигурацию сервиса
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/serviceConfig")
    public Response getServiceCongig(){	
	return Response.ok(notifyTimer.getServiceConf()).build();
    }
    
    /**
     * Выполняет сброс статуса нотификации 
     * @param gsonStr - нотификация в JSON
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    @Path("/clearNotifyStatus")
    public Response clearNotifyStatus(String gsonStr){
	LOGGER.log(Level.INFO, "REST_SERV clearNotifyStatus start from gson={0}", gsonStr);
	Gson gson = new Gson();
	NotifyData notifyData = gson.fromJson(gsonStr, NotifyData.class);
	if (notifyData == null){
	    return Response.status(Response.Status.BAD_REQUEST).build();
	}
	Set<String> errors = notifyPersist.clearNotifyStatus(notifyData);
	LOGGER.log(Level.INFO, "REST_SERV clearNotifyStatus completed! Errors count={0}", errors.size());
	return Response.ok(errors).build();
    }
    
    /**
     * Возвращает xml нотификации
     * @param notifyId
     * @return 
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/loadNotifyXml")
    public Response getNotifyXml(@QueryParam("notifyId") String notifyId){	
	return Response.ok(notifyPersist.loadNotifyXml(notifyId)).build();
    }
}
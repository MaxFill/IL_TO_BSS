package ru.rt.conf;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class RESTAppConf extends Application {
    /*
    	resources.add(ru.rt.fsom.wfc.wfcservice.service.users.UserService.class);
	resources.add(ru.rt.fsom.wfc.wfcservice.service.ticket.attached.AttachedService.class);
	resources.add(ru.rt.fsom.wfc.wfcservice.service.ticket.group.GroupService.class);
	resources.add(ru.rt.fsom.wfc.wfcservice.service.ticket.TicketService.class);
    */	
    @Override
    public Set<Class<?>> getClasses() {
	Set<Class<?>> resources = new java.util.HashSet<>();
	addRestResourceClasses(resources);
	return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
	resources.add(ru.rt.notify.NotifyService.class);
    }
    
}
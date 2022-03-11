package ru.rt.dict;

import java.util.logging.Logger;

/**
 * Словарь параметров
 * @author Maksim.Filatov
 */
public final class Params {

    private Params() {
    }    

    public static final String LOGGER_NAME = "IL_TO_BSS";
    public static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);	
    public static final String PARAM_TASK_ID = "task_id";
    public static final String ALL = "all";
    public final static String NEI = "NEI";
    public final static String FSOM = "FSOM";
    
    public final static String TYPE_CREATE = "submitOrder";
    public final static String TYPE_CANCEL = "cancelOrder";
    public final static String TYPE_CHANGE = "amendOrder";
    public final static String TYPE_INFO = "queryOrder";
    
    public final static String ACTION = "ACTION";
    public final static String ATTR = "ATTR";
    public final static String ATTACHMENT = "ATTACHMENT";
    public final static String AUTHOR = "AUTHOR";
    public final static String AFFILIATE = "AFFILIATE";
    public final static String APPOINTMENT_ID = "APPOINTMENT_ID";
    public final static String BRANCH = "BRANCH";
    public final static String DATE = "DATE";
    public final static String CATALOG_ID = "CATALOG_ID";
    public final static String CALLBACK_ENDPOINT = "CALLBACK_ENDPOINT";
    public final static String COMPLETION_DATE = "COMPLETION_DATE";
    public static final String CENTRALOFFICE_ID = "CENTRALOFFICE_ID";
    public static final String COMMISSIONING_DATE = "COMMISSIONING_DATE";
    public final static String COMMENT = "COMMENT";
    public final static String CATEGORY = "CATEGORY";
    public final static String CODE = "CODE";
    public final static String COMMENTER = "COMMENTER";
    public static final String CREATIONDATE = "CREATIONDATE";
    public final static String EXPECTED_COMPLETION_DATE = "EXPECTED_" + COMPLETION_DATE;
    public final static String EXPECTED_START_DATE = "EXPECTED_START_DATE";
    public final static String EXTERNAL_SPEC_ID = "EXTERNAL_SPEC_ID";
    public final static String EQUIPMENT = "EQUIPMENT";
    public final static String DESCRIPTION = "DESCRIPTION";
    public final static String EXCLUSIVEUSE = "EXCLUSIVEUSE";
    public final static String AVAILABLE_CAPACIY = "AVAILABLE_CAPACIY";    
    public static final String EXTRACAPACITY = "EXTRACAPACITY";
    public final static String RESOLUTION = "RESOLUTION";
    public final static String HEADER = "HEADER";
    public static final String HAS_PROJECT_FIBERLINK = "HASPROJECTFIBERLINK";
    public final static String FILEEXTENSION = "FILEEXTENSION";
    public final static String FILENAME = "FILENAME";
    public final static String GROUP_NAME = "GROUP_NAME";
    public final static String GATE_NAME = "GATE_NAME";
    public final static String ID = "ID";
    public final static String INSTANCE_ID = "INSTANCE_ID";
    public final static String ISCHANGED = "ISCHANGED";
    public final static String ISUPDATE = "ISUPDATE";
    public final static String ISINHERITABLE = "ISINHERITABLE";
    public final static String INTERNAL_SPEC_ID = "INTERNAL_SPEC_ID";
    public final static String LISTSIZE = "LISTSIZE";
    public final static String MODE = "MODE";
    public final static String NETYPE = "NETYPE";
    public final static String NAME = "NAME";
    public final static String NOTIFICATION = "NOTIFICATION";
    public final static String NOTIFY_ID = "NOTIFY_ID";
    public final static String PARTY = "PARTY";
    public final static String RESULT_CODE = "RESULT_CODE";
    public final static String RESULT_TEXT = "RESULT_TEXT";
    public final static String RESTRICTION = "RESTRICTION";
    public final static String REGISTER = "REGISTER";
    public final static String ROLE = "ROLE";
    public final static String STATUS = "STATUS";
    public final static String STATUS_COMPLETED = "COMPLETED";
    public final static String STATUS_REJECTED = "REJECTED";
    public final static String STATUS_SO_TIMEOUT = "SO_TIMEOUT";
    public final static String STATUS_EMPTY = "STATUS_EMPTY";
    public final static String STATE = "STATE";
    public final static String SPEC = "SPECIF";
    public final static String SERVICE_GUID = "SERVICE_GUID";
    public final static String START_DATE = "START_DATE";
    public final static String TIMES_TAMP = "TIMES_TAMP";
    
    public final static String TEXT = "TEXT";
    public final static String TYPE = "TYPE";
    public final static String TASK_ID = "TASK_ID"; 
    public final static String ORIGINATOR = "ORIGINATOR";
    public final static String VALUE = "VALUE";
    public final static String VERSION = "VERSION";
    public final static String URL = "URL";
    public final static String OMS_ID = "ORDER_OMS";
    
    public final static String ORDER = "ORDER";
    public final static String ORDER_ACKNOWLEDGEMENT = "ORDER_ACKNOWLEDGEMENT";
    public final static String ORDER_ATTACHMENT = "ORDER_ATTACHMENT";
    public final static String ORDER_AFFILIATE = "ORDER_AFFILIATE";
    public final static String ORDER_APPOINTMENT_ID = "ORDER_APPOINTMENT_ID";
    public final static String ORDER_BRANCH = "ORDER_BRANCH";    
    public final static String ORDER_CHANNEL = "ORDER_CHANNEL";  
    public final static String ORDER_DATE = "ORDER_DATE";    
    public final static String ORDER_LOCATION = "ORDER_LOCATION";
    public final static String ORDER_ID = "ORDER_ID";
    public final static String ORDER_ITEM = "OI";
    public final static String ORDER_PARENT_ID = "ORDER_PARENT_ID";
    public final static String ORDER_PRIORITY = "ORDER_PRIORITY";
    public final static String ORDER_REQUESTED_START_DATE = "ORDER_REQUESTED_START_DATE";
    public final static String ORDER_REQUIRED_DATE = "ORDER_REQUIRED_DATE";
    public final static String ORDER_REASON_CODE = "ORDER_REASON_CODE";
    public final static String ORDER_REASON_TEXT = "ORDER_REASON_TEXT";
    public final static String ORDER_STATE = "ORDER_STATE";
    public final static String ORDER_STATUS = "ORDER_STATUS";
    public final static String ORDER_TASK = "ORDER_TASK";
    public final static String ORDER_TYPE = "ORDER_TYPE";
    public final static String ORDER_NOTIF = "ORDER_NOTIF";
    
    public final static String ORDER_OMS_ID = "ORDER_OMS_ID";
    public final static String ORDER_OMS_URL = "ORDER_OMS_URL";
    public final static String ORDER_RESULT_CODE = "ORDER_RESULT_CODE";
    public final static String ORDER_RESULT_TEXT = "ORDER_RESULT_TEXT";
    public final static String ORDER_EXPECTED_COMPLETION_DATE = "ORDER_EXPECTED_COMPLETION_DATE";
    public final static String ORDER_START_DATE = "ORDER_START_DATE";
    public final static String ORDER_COMPLETION_DATE = "ORDER_COMPLETION_DATE";
    
    public final static String RECEIVER = "RECEIVER";
    public final static String REQUEST_ID = "REQUEST_ID";
    public final static String REQUEST_MODIFY = "modifyRequest";
    
    public final static String USER = "user";
}
package ru.rt.conf;

import ru.rt.dict.Params;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.crypto.spec.SecretKeySpec;
import ru.rt.utils.Utils;

/**
 *
 * @author Maksim.Filatov
 */
@Singleton
@Startup
@LocalBean
public class Conf {        
    public static final String NOTIFY_SAFE_FOLDER = "/opt/wildfly/standalone/log/safe/";
    public static final int HTTP_TIMEOUT_CONNECT = 4000;   // мил.сек 
    
    private static final int TIMER_LAUNCH_FREQUENCY = 10000;  //частота запуска таймера нотификаций в милисекундах 
    private static final String DATA_SOURCE_NAME = "java:jboss/datasources/PostgresDS";    
    private static final String DEFAULT_USE_SSL = "true"; 
    private static final String DEFAULT_ENCODING = "UTF-8";
    
    private static final int DEFAULT_COUNT_ERR_FOR_BLOCKED = 5;	    //кол-во ошибок после чего обработчик временно блокируется 
    private static final int DEFAULT_COUNT_FOR_RESTART = 15;	    //кол-во попыток, после чего временно заблокированный обработчик будет перезапущен
    private static final int DEFAULT_COUNT_FOR_LOCKDOWN = 5;	    //кол-во попыток, после чего обработчик будет окончательно заблокирован 
    private static final int TIMEOUT_SOAP_RECEIVE = 12000;	    //мил.сек на сессисю TODO: move this to resource bundle
    private static final int TIMEOUT_SOAP_CONNECT = 12000;	    //мил.сек на соединение TODO: move this to resource bundle
    private static final int ERR_MIN_LENGHT = 1024;    
    
    private DataSource ds;
    private ResourceBundle properties;
    private SecretKeySpec secretKey = null;
    private String serviceGuid;
    private String versionInfo;
    
    private final Map<String, UrlConf> bssConf1 = new ConcurrentHashMap<>();	//конфигурации сервисов BSS-CRM
    
    @PostConstruct
    private void init() {
	initConnectionPool();
	initConfFile();
	initBssConf();
	initServiceGuid();
	loadVersionInfo();
    }
    
    public long getTimerIntervalLaunch(){
	return getIntPropertyByKey("NOTIFY_TIMER_LAUNCH_FREQUENCY", TIMER_LAUNCH_FREQUENCY);
    }
    
    public Connection getJdbcConnection() throws SQLException{		
	if (ds != null){ 
	    return ds.getConnection();
	} else {
	    throw new SQLException("DataSource is not initialized!");
	}
    }

    public String getLoginCRM(final String url) { 
	UrlConf urlConf = bssConf1.get(url);	    
	if (urlConf != null){
	    return urlConf.getLogin();
	}
	return getStringPropertyByKey("crm_auth_login", "FSOM"); 
    }
    
    public String getPasswordCRM(final String url) { 	
	UrlConf urlConf = bssConf1.get(url);
	if (urlConf != null){
	    try {
		return Utils.decrypt(urlConf.getPwl(), getSecretKey());
	    } catch (GeneralSecurityException | IOException ex) {
		Params.LOGGER.log(Level.SEVERE, null, ex);
	    }
	}
	Params.LOGGER.log(Level.WARNING, "Получен дефолтный пароль для {0}", url);
	return getStringPropertyByKey("crm_auth_pwd", "1111");
    }
    
    public boolean useBasicHttpAuth(final String url) {
	return bssConf1.get(url) != null;
    }
    
    public String getMailPort(){
	return getStringPropertyByKey("MAIL_PORT", "");
    }    
    public String getMailEncoding(){
	return getStringPropertyByKey("MAIL_ENCODING", DEFAULT_ENCODING);
    }  
    public boolean getMailUseSSL(){
	String value = getStringPropertyByKey("MAIL_USE_SSL", DEFAULT_USE_SSL);
	return Boolean.valueOf(value);
    }       
    public String getMailUser(){
        return getStringPropertyByKey("MAIL_LOGIN", "");
    }    
    public String getMailPassword(){
        return getStringPropertyByKey("MAIL_PASSWORD", "");
    }            
    public String getMailHostName(){
        return getStringPropertyByKey("MAIL_SERVER", "");
    }     
    
    public int getCountErrForLock(){
	return getIntPropertyByKey("COUNT_ERR_FOR_BLOCKED_URL", DEFAULT_COUNT_ERR_FOR_BLOCKED);
    }
    
    public int getCountAttemptsForRestart(){
	return getIntPropertyByKey("COUNT_ATTEMPS_FOR_RESTART_URL", DEFAULT_COUNT_FOR_RESTART);
    }
    
    public int getCountAttemptsForLockdown(){
	return  getIntPropertyByKey("COUNT_ATTEMPS_FOR_LOCKDOWN_URL", DEFAULT_COUNT_FOR_LOCKDOWN);
    }     

    public Integer getTimeOutRequest() {
	return TIMEOUT_SOAP_RECEIVE;
    }    

    public Integer getTimeOutConnect() {
	return TIMEOUT_SOAP_CONNECT;
    }

    public int getErrMinLenght(){
	return ERR_MIN_LENGHT;
    }
    
    public String getServiceGuid(){
	return serviceGuid;
    }        
    
    public String getVersionInfo() {
	return versionInfo;
    }
    
    /* *** privates *** */    
    
    private String getServiceUrl(){
	try {
	    InetAddress inetAddress = InetAddress.getLocalHost();	    
	    return inetAddress.getHostAddress();
	} catch (UnknownHostException ex) {
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	}
	return null;
    }        
	
    private void loadVersionInfo() {	
	Enumeration resEnum;
	try {
	    resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
	    while (resEnum.hasMoreElements()) {
		try {
		    URL url = (URL)resEnum.nextElement();
		    InputStream is = url.openStream();
		    if (is != null) {
			Manifest manifest = new Manifest(is);
			Attributes attrs = manifest.getMainAttributes();
			if (attrs.getValue("build_name") != null){
			    StringBuilder sb = new StringBuilder();
			    sb.
				append("{").
				append("name:'").append(attrs.getValue("build_name")).append("', ").
				append("version:'").append(attrs.getValue("build_version")).append("', ").
				append("date:'").append(attrs.getValue("build_date")).append("', ").
				append("specification:'").append(attrs.getValue("build_specif")).append("'").
				append("}");
			    Params.LOGGER.log(Level.INFO, "Version info loaded: {0}", sb.toString());
			    versionInfo = sb.toString();
			}
		    }		    
		}
		catch (IOException ex) {
		    Params.LOGGER.log(Level.SEVERE, null, ex);
		}
	    }
	} catch (IOException ex){
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	}
    }
      	
    private void initServiceGuid(){
	String ipv4 = getServiceUrl();
	Params.LOGGER.log(Level.INFO, "IP Address: {0}", ipv4);
	serviceGuid = UUID.nameUUIDFromBytes(ipv4.getBytes()).toString();
	Params.LOGGER.log(Level.INFO, "Service GUID={0}", serviceGuid);
    }
    
    private void initConnectionPool(){
	try {
	    InitialContext initContext = new InitialContext();
	    ds = (DataSource) initContext.lookup(DATA_SOURCE_NAME);
	} catch (NamingException ex) {
	    Params.LOGGER.log(Level.SEVERE, "error init db connection pool! ", ex);
	}
    }    
    
    private void initBssConf(){
	try {       
	    String fileConfPath = System.getProperty("auth_config_file");
	    Params.LOGGER.log(Level.INFO, "Выполняется загрузка конфигурации bss из файла {0} ...", fileConfPath);
	    File configFile = new File(fileConfPath);	    
	    ObjectMapper om = new ObjectMapper(new YAMLFactory());
	    BssConf bssConf = om.readValue(configFile, BssConf.class);
	    if (bssConf != null){
		loadConf(bssConf);
		Params.LOGGER.log(Level.INFO, "Загрузка конфигурации выполнена!", fileConfPath);
	    } else {
		Params.LOGGER.log(Level.INFO, "Не удалось загрузить конфигурацию!", fileConfPath);
	    }
	} catch (Exception ex){
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	}
    }    
    
    private void initConfFile(){
        try {
            File props_path = new File(System.getProperty("jboss.server.config.dir"));
            URL[] urls = {props_path.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            properties = ResourceBundle.getBundle("FSOM", Locale.ROOT, loader);	    
        } catch (MalformedURLException ex) {
            Params.LOGGER.log(Level.SEVERE, "Error loading config file!", ex);
        }
    }               
    
    private void loadConf(BssConf bssConf){
	List<UrlConf> params = bssConf.getParams();
	bssConf1.clear();	
	AtomicBoolean needSaveConf = new AtomicBoolean(Boolean.FALSE);	
	params.forEach(urlConf -> {	    
	    if (!urlConf.getEncrypt()){ //если пароль не зашифрован, то его нужно зашифровать
		try {
		    urlConf.setPwl(Utils.encrypt(urlConf.getPwl(), getSecretKey()));
		    urlConf.setEncrypt(Boolean.TRUE);
		    needSaveConf.getAndSet(Boolean.TRUE);
		} catch (GeneralSecurityException | UnsupportedEncodingException ex) {
		    Params.LOGGER.log(Level.SEVERE, null, ex);
		}
	    }	    
	    bssConf1.put(urlConf.getUrl(), urlConf);
	    Params.LOGGER.log(Level.INFO, "Выполнена загрузка параметров конфигурации для URL {0} ", new Object[]{urlConf.getUrl()});
	});	
	if (needSaveConf.get()){
	    saveBssConf(bssConf);
	}
    }
    
    private SecretKeySpec getSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException{
	if (secretKey == null){
	    String pwd = System.getenv("FSOM_CODE");	    
	    byte[] salt = new String("12345678").getBytes();
	    int iterationCount = 40000;
	    int keyLength = 128;
	    secretKey = Utils.createSecretKey(pwd.toCharArray(), salt, iterationCount, keyLength);
	}
	return secretKey;
    }
    
    private void saveBssConf(BssConf bssConf){
	try {
	    String fileConfPath = System.getProperty("auth_config_file");
	    Params.LOGGER.log(Level.INFO, "begin save BSS config to {0} ", fileConfPath);
	    File configFile = new File(fileConfPath);	    
	    ObjectMapper om = new ObjectMapper(new YAMLFactory());
	    om.writeValue(configFile, bssConf);	    
	    Params.LOGGER.log(Level.INFO, "BSS auth config file saved successfully!", fileConfPath);	    
	} catch (Exception ex){
	    Params.LOGGER.log(Level.SEVERE, null, ex);
	}
    }
    
    private String getStringPropertyByKey(String key, String defaultValue){
        String result = defaultValue;
        if (null != properties){
            try {
                result = properties.getString(key);
            }
            catch (MissingResourceException | ClassCastException ignoreIt){}//ignore this and return default value
        }
        return result;
    } 
    
    private int getIntPropertyByKey(final String key, int defaultValue){       
        int result = defaultValue;
        if (null != properties){
            try {
		result = Integer.valueOf(properties.getString(key));
            } catch (MissingResourceException | ClassCastException ex){
		Params.LOGGER.log(Level.SEVERE, null, ex);
	    }//ignore this and return default value
        }
        return result;
    }	
}
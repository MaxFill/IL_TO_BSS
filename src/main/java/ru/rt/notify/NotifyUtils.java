package ru.rt.notify;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import ru.rt.conf.Conf;
import static ru.rt.dict.Params.LOGGER;
import ru.rt.oms.OMSOrderNotificationWebService;
import ru.rt.oms.OMSOrderNotificationWebService_Service;

@Stateless
public class NotifyUtils {
    private static final int ERR_MIN_LENGHT = 1024;
    private static final int HTTP_TIMEOUT_CONNECT = 7000;   // мил.сек
    private static final String WSDL_URL = "http://localhost:8080/RTFFASYNCAPI_v01.wsdl";

    @EJB private Conf conf;
    
    public synchronized OMSOrderNotificationWebService initWebService(String bssUrl, Set<String> errors) {
        final URL url = validateURL(bssUrl, errors);
        if (url == null) {
            LOGGER.log(Level.INFO, "{0} Не удалось установить соединение или указан некорректный URL!", new Object[]{bssUrl});
            return null;
        }
        LOGGER.log(Level.INFO, "{0} Инициализация web клиента ... выполняется подключение к серверу CRM ", new Object[]{bssUrl});
        OMSOrderNotificationWebService bssService = null;
        try {
            OMSOrderNotificationWebService_Service service = new OMSOrderNotificationWebService_Service(new URL(WSDL_URL), new QName("http://oms.rt.ru/", "OMSOrderNotificationWebService"));
            bssService = service.getOMSOrderNotificationWebServicePort();
	    Map<String, Object> ctx = ((BindingProvider) bssService).getRequestContext();
	    ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, bssUrl);
	    ctx.put("javax.xml.ws.client.receiveTimeout", conf.getTimeOutRequest());       // Timeout in millis
	    ctx.put("javax.xml.ws.client.connectionTimeout", conf.getTimeOutConnect());    // Timeout in millis
	    LOGGER.log(Level.INFO, "{0} Подключение к CRM выполнено, таймаут = {1} ms.", new Object[]{bssUrl, conf.getTimeOutConnect()});
	    if (conf.useBasicHttpAuth(bssUrl)) {
		//LOGGER.log(Level.INFO, "{0} Инициализация аутентификации... ", new Object[]{bssUrl});
		ctx.put(BindingProvider.USERNAME_PROPERTY, conf.getLoginCRM(bssUrl));
		ctx.put(BindingProvider.PASSWORD_PROPERTY, conf.getPasswordCRM(bssUrl));
		//LOGGER.log(Level.INFO, "{0} Инициализация аутентификации выполнена {1} {2}", new Object[]{bssUrl, conf.getLogin(bssUrl), conf.getPassword(bssUrl)});
	    } else{
		//LOGGER.log(Level.INFO, "{0} Аутентификация не используется", new Object[]{bssUrl});
	    }
        } catch (Exception ex) {
            String shortMsg = getShortMsg(ex.getMessage());
            LOGGER.log(Level.SEVERE, "{0} Ошибка при подключении к серверу CRM: {1}", new Object[]{bssUrl, ex.getMessage()});
            errors.add("При подключении к серверу CRM =" + bssUrl + " возникла ошибка: " + shortMsg);
            Integer respCode;
            if (bssService != null) {
                respCode = (Integer) ((BindingProvider) bssService).getResponseContext().get(MessageContext.HTTP_RESPONSE_CODE);
                LOGGER.log(Level.INFO, "{0} От CRM получен HTTP_RESPONSE_CODE = {1} ", new Object[]{bssUrl, respCode});
                errors.add(" От CRM получен HTTP_RESPONSE_CODE = " + respCode);
            }
        }
        return bssService;
    }
    
    public URL validateURL(final String bssUrl, Set<String> errors) {
        if (bssUrl == null) return null;
        try {
            int index = bssUrl.indexOf("?wsdl");
            if (index < 0) {
                errors.add("Адрес URL должен содержать ?wsdl !");
                LOGGER.log(Level.INFO, "{0} Адрес URL должен содержать ?wsdl !", new Object[]{bssUrl});
                return null;
            }
            //String clearURL = bssUrl.substring(0, bssUrl.indexOf("?wsdl"));
            LOGGER.log(Level.INFO, "{0} Выполняется проверка доступности BSS сервиса...", new Object[]{bssUrl});
            URL httpURL = new URL(bssUrl);
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection httpConn = (HttpURLConnection) httpURL.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(HTTP_TIMEOUT_CONNECT);
            httpConn.setReadTimeout(HTTP_TIMEOUT_CONNECT);
            //LOGGER.log(Level.INFO, "{0} Получение конфигурационных параметров ... ", new Object[]{bssUrl});
            if (conf.useBasicHttpAuth(bssUrl)) {
                LOGGER.log(Level.INFO, "{0} Загрузка данных для аутентификации ... ", new Object[]{bssUrl});
                String encoded = Base64.getEncoder().encodeToString((conf.getLoginCRM(bssUrl) + ":" + conf.getPasswordCRM(bssUrl)).getBytes(StandardCharsets.UTF_8));
                //LOGGER.log(Level.INFO, "{0} Для поключения к сервису BSS будет использована авторизация: {1}", new Object[]{bssUrl, encoded});
                httpConn.setRequestProperty("Authorization", "Basic " + encoded);
            }
            //LOGGER.log(Level.INFO, "{0} Пытаемся соединиться... ", new Object[]{bssUrl});
            int responseCode = httpConn.getResponseCode();
            LOGGER.log(Level.INFO, "{0} Получен ResponseCode={1}", new Object[]{bssUrl, responseCode});
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_BAD_METHOD && responseCode != HttpURLConnection.HTTP_UNAUTHORIZED) {
                String errMsg = "HttpURLConnection Error: responseCode=" + responseCode + ":" + httpConn.getResponseMessage();
                errors.add(errMsg);
                throw new IOException(errMsg);
            }
            return new URL(bssUrl);
        } catch (SocketTimeoutException ex) {
            errors.add("Не удалось установить соединение: " + getShortMsg(ex.getMessage()));
            LOGGER.log(Level.INFO, "{0} Не удалось установить соединение: {1}", new Object[]{bssUrl, getShortMsg(ex.getMessage())});
        } catch (MalformedURLException ex) {
            errors.add("Ошибка парсинга URL: " + getShortMsg(ex.getMessage()));
            LOGGER.log(Level.INFO, "{0} Ошибка парсинга URL: {1}", new Object[]{bssUrl, getShortMsg(ex.getMessage())});
        } catch (IOException ex) {
            errors.add("Ошибка соединения с URL:" + getShortMsg(ex.getMessage()));
            LOGGER.log(Level.INFO, "{0} Ошибка соединения с URL: {1}", new Object[]{bssUrl, getShortMsg(ex.getMessage())});
        } catch (Exception ex) {
            errors.add("Ошибка выполнения! См лог файл ");
            LOGGER.log(Level.INFO, null, ex);
        }
        return null;
    }

    public String getShortMsg(String longMsg) {
        String shortMsg = "";
        if (longMsg != null) {
            shortMsg = longMsg.substring(0, Math.min(ERR_MIN_LENGHT, longMsg.length()));
        }
        return shortMsg;
    }

}

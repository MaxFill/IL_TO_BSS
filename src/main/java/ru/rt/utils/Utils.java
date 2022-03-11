package ru.rt.utils;

import java.io.IOException;
import org.w3c.dom.Document;
import ru.rt.dict.Params;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Duration;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Утилиты
 * @author Maksim.Filatov
 */
public final class Utils {
    public static final BigInteger UNKNOWN_MODE_VALUE = BigInteger.valueOf(-2L);
    public static final BigInteger WRONG_REQUEST = BigInteger.valueOf(-1L);
    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    public static final Long STD_PRIORITY = 1L;    
    
    public static String getDifferenceTime(Date dateStart, Date dateEnd){
	if (dateStart == null || dateEnd == null) return "";
	try {
	LocalDateTime ldStart = LocalDateTime.ofInstant(dateStart.toInstant(), ZoneId.systemDefault());
	LocalDateTime ldEnd = LocalDateTime.ofInstant(dateEnd.toInstant(), ZoneId.systemDefault());
	Duration daration = Duration.between(ldStart, ldEnd);
	long seconds = daration.getSeconds();
	long hour = seconds / SECONDS_PER_HOUR;
	long minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
	long secs = (seconds % SECONDS_PER_MINUTE);
	StringBuilder sb = new StringBuilder();
	sb.append(hour).append(":").append(minutes).append(":").append(secs);
	return sb.toString();
	} catch (Exception  ex){
	    Params.LOGGER.log(Level.INFO, "Error {0} dateStart={1} dateEnd={2}", new Object[]{ex.getMessage(), dateStart, dateEnd});
	}
	return "";
    }
    
    public static String generateUID(){
	return UUID.randomUUID().toString();
    }
    
    public static Map<String, String> createDataResult(final String code, Set<String> messages, final String notifyId){	
	Map<String, String> result = new HashMap<>();
	result.put(Params.RESULT_CODE, code); 
	result.put(Params.RESULT_TEXT, String.join(",", messages));
	result.put(Params.NOTIFY_ID, notifyId);
	return result;
    }
	
    public static String formatStrToStrDate(String sourceDate){
	StringBuilder sb = new StringBuilder(sourceDate).insert(4, "-").insert(7, "-").insert(10, "T").insert(13, ":").insert(16, ":").insert(18, "Z");
	return sb.toString();
    }
    
    public static String getCurrentDateAsString(){
	Date date = Calendar.getInstance().getTime();
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	return dateFormat.format(date);
    }
     
    public static String getDateAsString(Date date){	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return dateFormat.format(date);
    }

    /*FIXME:
        Why just not using new java.sql.Timestamp(System.currentTimeMillis()); ?
        Why we need getCurrentTimeStamp() method ?
     */
    public static java.sql.Timestamp getCurrentTimeStamp() {
	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());
    }
	
    public static XMLGregorianCalendar stringToXmlDateTime(String date) {
	if (date == null) return null;
        try {
           if (date.contains("-")) {
               return DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
           } else {
               return DatatypeFactory.newInstance().newXMLGregorianCalendar(formatStrToStrDate(date));
           }
        } catch (DatatypeConfigurationException | NullPointerException | IllegalArgumentException ex){
            Params.LOGGER.log(Level.SEVERE, "Got exception when dateTime converting: {0}", ex.getMessage());
            return null;
        }
    }
    
    public static String xmlDateTimeToString( XMLGregorianCalendar date) throws IllegalArgumentException {
        // for undefined date string representation is null string. 
        if (null == date) {return null;}
        // Any not null date must have time zone - check it.
        if (DatatypeConstants.FIELD_UNDEFINED == date.getTimezone()){
            throw new IllegalArgumentException("Date value:" + String.valueOf(date)+ " wrong. It have no time zone specification.");
        }
        else{
            return date.toString();        
        }
    }
            
    public static String documentToString(final Document doc) {
        // outputs a DOM structure to plain String
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }
    
    public static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    public static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
}

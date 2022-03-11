
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrderMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ASYNC"/>
 *     &lt;enumeration value="SYNC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderMode")
@XmlEnum
public enum OrderMode {


    /**
     * Асинхронный режим обработки заказа
     * 
     */
    ASYNC,

    /**
     * Синхронный режим обработки заказа
     * 
     */
    SYNC;

    public String value() {
        return name();
    }

    public static OrderMode fromValue(String v) {
        return valueOf(v);
    }

}

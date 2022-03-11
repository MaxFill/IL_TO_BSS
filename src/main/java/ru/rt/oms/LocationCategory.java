
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LocationCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LocationCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="STRICT"/>
 *     &lt;enumeration value="GEO"/>
 *     &lt;enumeration value="LANDMARK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LocationCategory")
@XmlEnum
public enum LocationCategory {


    /**
     * Точный адрес
     * 
     */
    STRICT,

    /**
     * Точные координаты адреса с указанием адресного элемента ориентира
     * 
     */
    GEO,

    /**
     * Адресный элемент ориентира с указанием навигации до точного адреса
     * 
     */
    LANDMARK;

    public String value() {
        return name();
    }

    public static LocationCategory fromValue(String v) {
        return valueOf(v);
    }

}


package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AttributeStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AttributeStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AC"/>
 *     &lt;enumeration value="CE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AttributeStatus")
@XmlEnum
public enum AttributeStatus {


    /**
     * Активное значение атрибута
     * 
     */
    AC,

    /**
     * Предыдущее (неактивное) значение атрибута
     * 
     */
    CE;

    public String value() {
        return name();
    }

    public static AttributeStatus fromValue(String v) {
        return valueOf(v);
    }

}

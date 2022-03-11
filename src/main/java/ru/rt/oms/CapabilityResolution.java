
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CapabilityResolution.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CapabilityResolution">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="READY"/>
 *     &lt;enumeration value="CHECK"/>
 *     &lt;enumeration value="UNAVAILABLE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CapabilityResolution")
@XmlEnum
public enum CapabilityResolution {

    READY,
    CHECK,
    UNAVAILABLE;

    public String value() {
        return name();
    }

    public static CapabilityResolution fromValue(String v) {
        return valueOf(v);
    }

}

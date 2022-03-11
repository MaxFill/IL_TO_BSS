
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EquipmentCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EquipmentCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OPTICAL_MUFF"/>
 *     &lt;enumeration value="OPTICAL_BOX"/>
 *     &lt;enumeration value="OPTICAL_CROSS"/>
 *     &lt;enumeration value="OPTICAL_CASE"/>
 *     &lt;enumeration value="SPLITTER"/>
 *     &lt;enumeration value="CABLE_RESERVE"/>
 *     &lt;enumeration value="CABLE_TRANSIT"/>
 *     &lt;enumeration value="ACTIVE_EQUIPMENT"/>
 *     &lt;enumeration value="KRT"/>
 *     &lt;enumeration value="CASE"/>
 *     &lt;enumeration value="CROSS"/>
 *     &lt;enumeration value="PP"/>
 *     &lt;enumeration value="BASE_STATION"/>
 *     &lt;enumeration value="RACK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EquipmentCategory")
@XmlEnum
public enum EquipmentCategory {

    OPTICAL_MUFF,
    OPTICAL_BOX,
    OPTICAL_CROSS,
    OPTICAL_CASE,
    SPLITTER,
    CABLE_RESERVE,
    CABLE_TRANSIT,
    ACTIVE_EQUIPMENT,
    KRT,
    CASE,
    CROSS,
    PP,
    BASE_STATION,
    RACK;

    public String value() {
        return name();
    }

    public static EquipmentCategory fromValue(String v) {
        return valueOf(v);
    }

}

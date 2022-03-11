
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LocationRegister.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LocationRegister">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HERMES"/>
 *     &lt;enumeration value="KLADR"/>
 *     &lt;enumeration value="GID"/>
 *     &lt;enumeration value="NW_BIS_AIR"/>
 *     &lt;enumeration value="Vlg"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LocationRegister")
@XmlEnum
public enum LocationRegister {


    /**
     * Внутренний справочник СУС Гермес
     * 
     */
    HERMES("HERMES"),

    /**
     * КЛАДР
     * 
     */
    KLADR("KLADR"),

    /**
     * ОРПОН
     * 
     */
    GID("GID"),

    /**
     * Справочник BIS в МРФ Северо-Запад
     * 
     */
    NW_BIS_AIR("NW_BIS_AIR"),

    /**
     * Справочник в МРФ Волга
     * 
     */
    @XmlEnumValue("Vlg")
    VLG("Vlg");
    private final String value;

    LocationRegister(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LocationRegister fromValue(String v) {
        for (LocationRegister c: LocationRegister.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

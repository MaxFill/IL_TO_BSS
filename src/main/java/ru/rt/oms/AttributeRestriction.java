package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AttributeRestriction.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AttributeRestriction">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="model"/>
 *     &lt;enumeration value="dynamic"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AttributeRestriction")
@XmlEnum
public enum AttributeRestriction {

    /**
     * Атрибут определяется сервисной моделью
     * 
     */
    @XmlEnumValue("model")
    MODEL("model"),

    /**
     * Атрибут не определяется сервисной моделью
     * 
     */
    @XmlEnumValue("dynamic")
    DYNAMIC("dynamic");
    private final String value;

    AttributeRestriction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AttributeRestriction fromValue(String v) {
        for (AttributeRestriction c: AttributeRestriction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
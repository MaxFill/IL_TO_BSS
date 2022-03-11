
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrderCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="B2C"/>
 *     &lt;enumeration value="KKFU"/>
 *     &lt;enumeration value="KKMU"/>
 *     &lt;enumeration value="KKRU"/>
 *     &lt;enumeration value="B2G"/>
 *     &lt;enumeration value="B2O"/>
 *     &lt;enumeration value="B2B"/>
 *     &lt;enumeration value="EXTENSION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderCategory")
@XmlEnum
public enum OrderCategory {

    @XmlEnumValue("B2C")
    B_2_C("B2C"),
    KKFU("KKFU"),
    KKMU("KKMU"),
    KKRU("KKRU"),
    @XmlEnumValue("B2G")
    B_2_G("B2G"),
    @XmlEnumValue("B2O")
    B_2_O("B2O"),

    /**
     * Корпоративный сегмент без детализации
     * 
     */
    @XmlEnumValue("B2B")
    B_2_B("B2B"),

    /**
     * Технологический заказ (развитие)
     * 
     */
    EXTENSION("EXTENSION");
    private final String value;

    OrderCategory(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OrderCategory fromValue(String v) {
        for (OrderCategory c: OrderCategory.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

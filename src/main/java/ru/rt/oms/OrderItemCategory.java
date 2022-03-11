
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderItemCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrderItemCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Product"/>
 *     &lt;enumeration value="CFS"/>
 *     &lt;enumeration value="CRFS"/>
 *     &lt;enumeration value="RFS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderItemCategory")
@XmlEnum
public enum OrderItemCategory {


    /**
     * Комплексная услуга, предоставляемая клиенту
     * 
     */
    @XmlEnumValue("Product")
    PRODUCT("Product"),

    /**
     * Атомарная услуга, предоставляемая клиенту
     * 
     */
    CFS("CFS"),

    /**
     * Композитная ресурсная услуга
     * 
     */
    CRFS("CRFS"),

    /**
     * Ресурсная услуга
     * 
     */
    RFS("RFS");
    private final String value;

    OrderItemCategory(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OrderItemCategory fromValue(String v) {
        for (OrderItemCategory c: OrderItemCategory.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

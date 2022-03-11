
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderItemState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrderItemState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INPROGRESS"/>
 *     &lt;enumeration value="PENDING"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="CANCELLED"/>
 *     &lt;enumeration value="ONHOLD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderItemState")
@XmlEnum
public enum OrderItemState {


    /**
     * Компонент в процессе обработки
     * 
     */
    INPROGRESS,

    /**
     * Компонент в ожидании внешнего события
     * 
     */
    PENDING,

    /**
     * Компонент успешно обработан
     * 
     */
    COMPLETED,

    /**
     * Компонент отменен
     * 
     */
    CANCELLED,

    /**
     * Компонент остановлен из-за ошибки и находится в ожидании решения проблемы
     * 
     */
    ONHOLD;

    public String value() {
        return name();
    }

    public static OrderItemState fromValue(String v) {
        return valueOf(v);
    }

}

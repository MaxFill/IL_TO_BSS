
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrderState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RECEIVED"/>
 *     &lt;enumeration value="ACKNOWLEDGED"/>
 *     &lt;enumeration value="REJECTED"/>
 *     &lt;enumeration value="INPROGRESS"/>
 *     &lt;enumeration value="AMENDING"/>
 *     &lt;enumeration value="CANCELLING"/>
 *     &lt;enumeration value="CANCELLED"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="FAILED"/>
 *     &lt;enumeration value="PARTIAL"/>
 *     &lt;enumeration value="POSTPONED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderState")
@XmlEnum
public enum OrderState {


    /**
     * Заказ получен, обработка не начата
     * 
     */
    RECEIVED,

    /**
     * Заказ получен, форматная проверка заказа проведена
     * 
     */
    ACKNOWLEDGED,

    /**
     * Заказ отвергнут по причине ошибок валидации
     * 
     */
    REJECTED,

    /**
     * Заказ корректен и находится на выполнении
     * 
     */
    INPROGRESS,

    /**
     * Получен запрос на изменение заказа
     * 
     */
    AMENDING,

    /**
     * Получен запрос на отмену заказа
     * 
     */
    CANCELLING,

    /**
     * Заказ отменен
     * 
     */
    CANCELLED,

    /**
     * Выполнение заказа успешно завершено, все компоненты заказа обработаны успешно
     * 
     */
    COMPLETED,

    /**
     * Выполнение заказа завершено неуспешно
     * 
     */
    FAILED,

    /**
     * Выполнение заказа завершено с успешной обработкой части заказа
     * 
     */
    PARTIAL,

    /**
     * Выполнение заказа приостановлено до получения сигнала о продолжении обработки
     * 
     */
    POSTPONED;

    public String value() {
        return name();
    }

    public static OrderState fromValue(String v) {
        return valueOf(v);
    }

}

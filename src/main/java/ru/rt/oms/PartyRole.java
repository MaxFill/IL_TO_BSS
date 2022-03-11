
package ru.rt.oms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PartyRole.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PartyRole">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CLIENT"/>
 *     &lt;enumeration value="INITIATOR"/>
 *     &lt;enumeration value="SALES"/>
 *     &lt;enumeration value="ACCOUNT"/>
 *     &lt;enumeration value="CONTACT"/>
 *     &lt;enumeration value="WORKER"/>
 *     &lt;enumeration value="KZ"/>
 *     &lt;enumeration value="KP"/>
 *     &lt;enumeration value="SZ"/>
 *     &lt;enumeration value="IP"/>
 *     &lt;enumeration value="SOLUTION"/>
 *     &lt;enumeration value="RESERVATION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PartyRole")
@XmlEnum
public enum PartyRole {


    /**
     * Клиент
     * 
     */
    CLIENT,

    /**
     * Оператор системы-источника запроса, инициатор запроса
     * 
     */
    INITIATOR,

    /**
     * Менеджер отдела продаж, инициатор продажи
     * 
     */
    SALES,

    /**
     * Счет клиента
     * 
     */
    ACCOUNT,

    /**
     * Контактная информация
     * 
     */
    CONTACT,

    /**
     * Исполнитель работ
     * 
     */
    WORKER,

    /**
     * Клиентская заявка в СУС
     * 
     */
    KZ,

    /**
     * Клиентский проект в СУС
     * 
     */
    KP,

    /**
     * Строительная заявка в СУС
     * 
     */
    SZ,

    /**
     * Инвестиционный проект в R12
     * 
     */
    IP,

    /**
     * Техническое решение в СУС
     * 
     */
    SOLUTION,

    /**
     * Бронь линейных данных в СЛТУ под техническое решение
     * 
     */
    RESERVATION;

    public String value() {
        return name();
    }

    public static PartyRole fromValue(String v) {
        return valueOf(v);
    }

}


package ru.rt.oms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Оповещение по выполнению запроса
 * 
 * <p>Java class for OrderNotification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderNotification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="notificationTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="notificationStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notificationText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notificationAttributes" type="{http://oms.rt.ru/}Attributes" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderNotification", propOrder = {

})
public class OrderNotification {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar notificationTimestamp;
    protected String notificationStatus;
    protected String notificationText;
    protected Attributes notificationAttributes;

    /**
     * Gets the value of the notificationTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getNotificationTimestamp() {
        return notificationTimestamp;
    }

    /**
     * Sets the value of the notificationTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setNotificationTimestamp(XMLGregorianCalendar value) {
        this.notificationTimestamp = value;
    }

    /**
     * Gets the value of the notificationStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationStatus() {
        return notificationStatus;
    }

    /**
     * Sets the value of the notificationStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationStatus(String value) {
        this.notificationStatus = value;
    }

    /**
     * Gets the value of the notificationText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationText() {
        return notificationText;
    }

    /**
     * Sets the value of the notificationText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationText(String value) {
        this.notificationText = value;
    }

    /**
     * Gets the value of the notificationAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Attributes }
     *     
     */
    public Attributes getNotificationAttributes() {
        return notificationAttributes;
    }

    /**
     * Sets the value of the notificationAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Attributes }
     *     
     */
    public void setNotificationAttributes(Attributes value) {
        this.notificationAttributes = value;
    }

}

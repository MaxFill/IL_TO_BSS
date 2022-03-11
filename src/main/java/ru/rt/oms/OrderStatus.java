
package ru.rt.oms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for OrderStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orderOMSId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orderState" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orderStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="orderExpectedCompletionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="orderCompletionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="orderNotifications" type="{http://oms.rt.ru/}OrderNotifications" minOccurs="0"/>
 *         &lt;element name="orderComments" type="{http://oms.rt.ru/}Comments" minOccurs="0"/>
 *         &lt;element name="orderParties" type="{http://oms.rt.ru/}OrderParties" minOccurs="0"/>
 *         &lt;element name="orderItems" type="{http://oms.rt.ru/}NotificationOrderItems" minOccurs="0"/>
 *         &lt;element name="orderAttributes" type="{http://oms.rt.ru/}Attributes" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderStatus", propOrder = {

})
public class OrderStatus {

    @XmlElement(required = true)
    protected String orderId;
    @XmlElement(required = true)
    protected String orderOMSId;
    @XmlElement(required = true)
    protected String orderState;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar orderStartDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar orderExpectedCompletionDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar orderCompletionDate;
    protected OrderNotifications orderNotifications;
    protected Comments orderComments;
    protected OrderParties orderParties;
    protected NotificationOrderItems orderItems;
    protected Attributes orderAttributes;

    /**
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the orderOMSId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderOMSId() {
        return orderOMSId;
    }

    /**
     * Sets the value of the orderOMSId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderOMSId(String value) {
        this.orderOMSId = value;
    }

    /**
     * Gets the value of the orderState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderState() {
        return orderState;
    }

    /**
     * Sets the value of the orderState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderState(String value) {
        this.orderState = value;
    }

    /**
     * Gets the value of the orderStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOrderStartDate() {
        return orderStartDate;
    }

    /**
     * Sets the value of the orderStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOrderStartDate(XMLGregorianCalendar value) {
        this.orderStartDate = value;
    }

    /**
     * Gets the value of the orderExpectedCompletionDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOrderExpectedCompletionDate() {
        return orderExpectedCompletionDate;
    }

    /**
     * Sets the value of the orderExpectedCompletionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOrderExpectedCompletionDate(XMLGregorianCalendar value) {
        this.orderExpectedCompletionDate = value;
    }

    /**
     * Gets the value of the orderCompletionDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOrderCompletionDate() {
        return orderCompletionDate;
    }

    /**
     * Sets the value of the orderCompletionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOrderCompletionDate(XMLGregorianCalendar value) {
        this.orderCompletionDate = value;
    }

    /**
     * Gets the value of the orderNotifications property.
     * 
     * @return
     *     possible object is
     *     {@link OrderNotifications }
     *     
     */
    public OrderNotifications getOrderNotifications() {
        return orderNotifications;
    }

    /**
     * Sets the value of the orderNotifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderNotifications }
     *     
     */
    public void setOrderNotifications(OrderNotifications value) {
        this.orderNotifications = value;
    }

    /**
     * Gets the value of the orderComments property.
     * 
     * @return
     *     possible object is
     *     {@link Comments }
     *     
     */
    public Comments getOrderComments() {
        return orderComments;
    }

    /**
     * Sets the value of the orderComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Comments }
     *     
     */
    public void setOrderComments(Comments value) {
        this.orderComments = value;
    }

    /**
     * Gets the value of the orderParties property.
     * 
     * @return
     *     possible object is
     *     {@link OrderParties }
     *     
     */
    public OrderParties getOrderParties() {
        return orderParties;
    }

    /**
     * Sets the value of the orderParties property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderParties }
     *     
     */
    public void setOrderParties(OrderParties value) {
        this.orderParties = value;
    }

    /**
     * Gets the value of the orderItems property.
     * 
     * @return
     *     possible object is
     *     {@link NotificationOrderItems }
     *     
     */
    public NotificationOrderItems getOrderItems() {
        return orderItems;
    }

    /**
     * Sets the value of the orderItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotificationOrderItems }
     *     
     */
    public void setOrderItems(NotificationOrderItems value) {
        this.orderItems = value;
    }

    /**
     * Gets the value of the orderAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Attributes }
     *     
     */
    public Attributes getOrderAttributes() {
        return orderAttributes;
    }

    /**
     * Sets the value of the orderAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Attributes }
     *     
     */
    public void setOrderAttributes(Attributes value) {
        this.orderAttributes = value;
    }

}

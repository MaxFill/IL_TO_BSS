
package ru.rt.oms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NotificationOrderItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotificationOrderItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="orderItemId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orderItemInstanceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderItemAction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderItemState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderItemParties" type="{http://oms.rt.ru/}OrderParties" minOccurs="0"/>
 *         &lt;element name="orderItemAttributes" type="{http://oms.rt.ru/}InheritableAttributes" minOccurs="0"/>
 *         &lt;element name="orderItemAppointmentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderItemResult" type="{http://oms.rt.ru/}OrderItemResult" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationOrderItem", propOrder = {

})
public class NotificationOrderItem {

    @XmlElement(required = true)
    protected String orderItemId;
    protected String orderItemInstanceId;
    protected String orderItemAction;
    protected String orderItemState;
    protected OrderParties orderItemParties;
    protected InheritableAttributes orderItemAttributes;
    protected String orderItemAppointmentId;
    protected OrderItemResult orderItemResult;

    /**
     * Gets the value of the orderItemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderItemId() {
        return orderItemId;
    }

    /**
     * Sets the value of the orderItemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderItemId(String value) {
        this.orderItemId = value;
    }

    /**
     * Gets the value of the orderItemInstanceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderItemInstanceId() {
        return orderItemInstanceId;
    }

    /**
     * Sets the value of the orderItemInstanceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderItemInstanceId(String value) {
        this.orderItemInstanceId = value;
    }

    /**
     * Gets the value of the orderItemAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderItemAction() {
        return orderItemAction;
    }

    /**
     * Sets the value of the orderItemAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderItemAction(String value) {
        this.orderItemAction = value;
    }

    /**
     * Gets the value of the orderItemState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderItemState() {
        return orderItemState;
    }

    /**
     * Sets the value of the orderItemState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderItemState(String value) {
        this.orderItemState = value;
    }

    /**
     * Gets the value of the orderItemParties property.
     * 
     * @return
     *     possible object is
     *     {@link OrderParties }
     *     
     */
    public OrderParties getOrderItemParties() {
        return orderItemParties;
    }

    /**
     * Sets the value of the orderItemParties property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderParties }
     *     
     */
    public void setOrderItemParties(OrderParties value) {
        this.orderItemParties = value;
    }

    /**
     * Gets the value of the orderItemAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link InheritableAttributes }
     *     
     */
    public InheritableAttributes getOrderItemAttributes() {
        return orderItemAttributes;
    }

    /**
     * Sets the value of the orderItemAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link InheritableAttributes }
     *     
     */
    public void setOrderItemAttributes(InheritableAttributes value) {
        this.orderItemAttributes = value;
    }

    /**
     * Gets the value of the orderItemAppointmentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderItemAppointmentId() {
        return orderItemAppointmentId;
    }

    /**
     * Sets the value of the orderItemAppointmentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderItemAppointmentId(String value) {
        this.orderItemAppointmentId = value;
    }

    /**
     * Gets the value of the orderItemResult property.
     * 
     * @return
     *     possible object is
     *     {@link OrderItemResult }
     *     
     */
    public OrderItemResult getOrderItemResult() {
        return orderItemResult;
    }

    /**
     * Sets the value of the orderItemResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderItemResult }
     *     
     */
    public void setOrderItemResult(OrderItemResult value) {
        this.orderItemResult = value;
    }

}

package ru.rt.oms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderStatusNotification", propOrder = {
    "originator",
    "receiver",
    "orderResult",
    "requestId",
    "order"
})
@XmlRootElement(name = "OrderStatusNotification")
public class OrderStatusNotification {

    @XmlElement(required = true)
    protected String originator;
    @XmlElement(required = true)
    protected String receiver;
    protected OrderResult orderResult;
    @XmlElement(required = true)
    protected String requestId;
    protected OrderStatus order;

    /**
     * Gets the value of the originator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginator() {
        return originator;
    }

    /**
     * Sets the value of the originator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginator(String value) {
        this.originator = value;
    }

    /**
     * Gets the value of the receiver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Sets the value of the receiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiver(String value) {
        this.receiver = value;
    }

    /**
     * Gets the value of the orderResult property.
     * 
     * @return
     *     possible object is
     *     {@link OrderResult }
     *     
     */
    public OrderResult getOrderResult() {
        return orderResult;
    }

    /**
     * Sets the value of the orderResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderResult }
     *     
     */
    public void setOrderResult(OrderResult value) {
        this.orderResult = value;
    }

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link OrderStatus }
     *     
     */
    public OrderStatus getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderStatus }
     *     
     */
    public void setOrder(OrderStatus value) {
        this.order = value;
    }

}

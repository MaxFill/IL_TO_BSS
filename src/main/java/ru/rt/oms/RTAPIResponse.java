
package ru.rt.oms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Абстрактный базовый тип для структуры ответа на запрос
 * 
 * <p>Java class for RTAPIResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RTAPIResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="originator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="receiver" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orderResult" type="{http://oms.rt.ru/}OrderResult"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RTAPIResponse", propOrder = {
    "originator",
    "receiver",
    "orderResult"
})
public abstract class RTAPIResponse {

    @XmlElement(required = true)
    protected String originator;
    @XmlElement(required = true)
    protected String receiver;
    @XmlElement(required = true)
    protected OrderResult orderResult;

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

}

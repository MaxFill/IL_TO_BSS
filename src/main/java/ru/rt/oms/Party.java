
package ru.rt.oms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Связанный объект, дополняющий запрос
 * 
 * <p>Java class for Party complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Party">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="partyRole" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="partyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="partyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="partyAttributes" type="{http://oms.rt.ru/}Attributes" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Party", propOrder = {

})
public class Party {

    @XmlElement(required = true)
    protected String partyRole;
    protected String partyId;
    protected String partyName;
    protected Attributes partyAttributes;

    /**
     * Gets the value of the partyRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartyRole() {
        return partyRole;
    }

    /**
     * Sets the value of the partyRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartyRole(String value) {
        this.partyRole = value;
    }

    /**
     * Gets the value of the partyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartyId() {
        return partyId;
    }

    /**
     * Sets the value of the partyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartyId(String value) {
        this.partyId = value;
    }

    /**
     * Gets the value of the partyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartyName() {
        return partyName;
    }

    /**
     * Sets the value of the partyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartyName(String value) {
        this.partyName = value;
    }

    /**
     * Gets the value of the partyAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Attributes }
     *     
     */
    public Attributes getPartyAttributes() {
        return partyAttributes;
    }

    /**
     * Sets the value of the partyAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Attributes }
     *     
     */
    public void setPartyAttributes(Attributes value) {
        this.partyAttributes = value;
    }

}

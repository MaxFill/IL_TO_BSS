
package ru.rt.oms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Местоположение (адресная характеристика)
 * 
 * <p>Java class for Location complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Location">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="locationId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locationCategory" type="{http://oms.rt.ru/}LocationCategory"/>
 *         &lt;element name="locationRegister" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="locationAttributes" type="{http://oms.rt.ru/}Attributes" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Location", propOrder = {

})
public class Location {

    protected String locationId;
    @XmlElement(required = true)
    protected LocationCategory locationCategory;
    @XmlElement(required = true)
    protected String locationRegister;
    protected Attributes locationAttributes;

    /**
     * Gets the value of the locationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * Sets the value of the locationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationId(String value) {
        this.locationId = value;
    }

    /**
     * Gets the value of the locationCategory property.
     * 
     * @return
     *     possible object is
     *     {@link LocationCategory }
     *     
     */
    public LocationCategory getLocationCategory() {
        return locationCategory;
    }

    /**
     * Sets the value of the locationCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocationCategory }
     *     
     */
    public void setLocationCategory(LocationCategory value) {
        this.locationCategory = value;
    }

    /**
     * Gets the value of the locationRegister property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationRegister() {
        return locationRegister;
    }

    /**
     * Sets the value of the locationRegister property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationRegister(String value) {
        this.locationRegister = value;
    }

    /**
     * Gets the value of the locationAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Attributes }
     *     
     */
    public Attributes getLocationAttributes() {
        return locationAttributes;
    }

    /**
     * Sets the value of the locationAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Attributes }
     *     
     */
    public void setLocationAttributes(Attributes value) {
        this.locationAttributes = value;
    }

}

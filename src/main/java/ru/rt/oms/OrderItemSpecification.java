
package ru.rt.oms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Спецификация компонента услуги
 * 
 * <p>Java class for OrderItemSpecification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderItemSpecification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="catalogId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;choice>
 *           &lt;element name="specId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="internalSpecId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="externalSpecId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="specVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="specName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderItemSpecification", propOrder = {
    "catalogId",
    "specId",
    "internalSpecId",
    "externalSpecId",
    "specVersion",
    "specName"
})
public class OrderItemSpecification {

    @XmlElement(required = true)
    protected String catalogId;
    protected String specId;
    protected String internalSpecId;
    protected String externalSpecId;
    protected String specVersion;
    protected String specName;

    /**
     * Gets the value of the catalogId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCatalogId() {
        return catalogId;
    }

    /**
     * Sets the value of the catalogId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCatalogId(String value) {
        this.catalogId = value;
    }

    /**
     * Gets the value of the specId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecId() {
        return specId;
    }

    /**
     * Sets the value of the specId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecId(String value) {
        this.specId = value;
    }

    /**
     * Gets the value of the internalSpecId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternalSpecId() {
        return internalSpecId;
    }

    /**
     * Sets the value of the internalSpecId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternalSpecId(String value) {
        this.internalSpecId = value;
    }

    /**
     * Gets the value of the externalSpecId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalSpecId() {
        return externalSpecId;
    }

    /**
     * Sets the value of the externalSpecId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalSpecId(String value) {
        this.externalSpecId = value;
    }

    /**
     * Gets the value of the specVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecVersion() {
        return specVersion;
    }

    /**
     * Sets the value of the specVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecVersion(String value) {
        this.specVersion = value;
    }

    /**
     * Gets the value of the specName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * Sets the value of the specName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecName(String value) {
        this.specName = value;
    }

}

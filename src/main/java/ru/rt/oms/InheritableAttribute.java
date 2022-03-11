
package ru.rt.oms;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for InheritableAttribute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InheritableAttribute">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;any processContents='lax' minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="restriction" type="{http://oms.rt.ru/}AttributeRestriction" default="model" />
 *       &lt;attribute name="status" type="{http://oms.rt.ru/}AttributeStatus" default="AC" />
 *       &lt;attribute name="isChanged" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="isUpdated" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="isInheritable" type="{http://www.w3.org/2001/XMLSchema}string" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InheritableAttribute", propOrder = {
    "content"
})
public class InheritableAttribute {

    @XmlMixed
    @XmlAnyElement(lax = true)
    protected List<Object> content;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "restriction")
    protected AttributeRestriction restriction;
    @XmlAttribute(name = "status")
    protected AttributeStatus status;
    @XmlAttribute(name = "isChanged")
    protected Boolean isChanged;
    @XmlAttribute(name = "isUpdated")
    protected Boolean isUpdated;
    @XmlAttribute(name = "isInheritable")
    protected String isInheritable;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link String }
     * {@link Element }
     * 
     * 
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the restriction property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeRestriction }
     *     
     */
    public AttributeRestriction getRestriction() {
        if (restriction == null) {
            return AttributeRestriction.MODEL;
        } else {
            return restriction;
        }
    }

    /**
     * Sets the value of the restriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeRestriction }
     *     
     */
    public void setRestriction(AttributeRestriction value) {
        this.restriction = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeStatus }
     *     
     */
    public AttributeStatus getStatus() {
        if (status == null) {
            return AttributeStatus.AC;
        } else {
            return status;
        }
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeStatus }
     *     
     */
    public void setStatus(AttributeStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the isChanged property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsChanged() {
        if (isChanged == null) {
            return false;
        } else {
            return isChanged;
        }
    }

    /**
     * Sets the value of the isChanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsChanged(Boolean value) {
        this.isChanged = value;
    }

    /**
     * Gets the value of the isUpdated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsUpdated() {
        if (isUpdated == null) {
            return false;
        } else {
            return isUpdated;
        }
    }

    /**
     * Sets the value of the isUpdated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsUpdated(Boolean value) {
        this.isUpdated = value;
    }

    /**
     * Gets the value of the isInheritable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsInheritable() {
        if (isInheritable == null) {
            return "false";
        } else {
            return isInheritable;
        }
    }

    /**
     * Sets the value of the isInheritable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsInheritable(String value) {
        this.isInheritable = value;
    }

}

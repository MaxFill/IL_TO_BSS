
package ru.rt.oms;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Объект вложений, дополняющий запрос
 * 
 * <p>Java class for Attachment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Attachment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="attachmentRegister" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attachmentType" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="creationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="author" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="header" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fileExtension" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="attachmentAttributes" type="{http://oms.rt.ru/}Attributes" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Attachment", propOrder = {

})
public class Attachment {

    @XmlElement(defaultValue = "FTP")
    protected String attachmentRegister;
    @XmlElement(required = true)
    protected BigInteger attachmentType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationDate;
    @XmlElement(required = true)
    protected String author;
    @XmlElement(name = "URL", required = true)
    protected String url;
    @XmlElement(required = true)
    protected String header;
    @XmlElement(required = true)
    protected String fileName;
    @XmlElement(required = true)
    protected String fileExtension;
    protected Attributes attachmentAttributes;

    /**
     * Gets the value of the attachmentRegister property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachmentRegister() {
        return attachmentRegister;
    }

    /**
     * Sets the value of the attachmentRegister property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachmentRegister(String value) {
        this.attachmentRegister = value;
    }

    /**
     * Gets the value of the attachmentType property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAttachmentType() {
        return attachmentType;
    }

    /**
     * Sets the value of the attachmentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAttachmentType(BigInteger value) {
        this.attachmentType = value;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationDate(XMLGregorianCalendar value) {
        this.creationDate = value;
    }

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURL() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURL(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeader(String value) {
        this.header = value;
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the fileExtension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * Sets the value of the fileExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileExtension(String value) {
        this.fileExtension = value;
    }

    /**
     * Gets the value of the attachmentAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Attributes }
     *     
     */
    public Attributes getAttachmentAttributes() {
        return attachmentAttributes;
    }

    /**
     * Sets the value of the attachmentAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Attributes }
     *     
     */
    public void setAttachmentAttributes(Attributes value) {
        this.attachmentAttributes = value;
    }

}

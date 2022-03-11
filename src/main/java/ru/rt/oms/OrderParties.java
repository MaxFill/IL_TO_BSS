
package ru.rt.oms;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * Список связанных объектов, характеризующих запрос
 * 
 * <p>Java class for OrderParties complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderParties">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="orderParty" type="{http://oms.rt.ru/}Party"/>
 *           &lt;element name="orderAttachment" type="{http://oms.rt.ru/}Attachment"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderParties", propOrder = {
    "orderPartyOrOrderAttachment"
})
public class OrderParties {

    @XmlElements({
        @XmlElement(name = "orderParty", type = Party.class),
        @XmlElement(name = "orderAttachment", type = Attachment.class)
    })
    protected List<Object> orderPartyOrOrderAttachment;

    /**
     * Gets the value of the orderPartyOrOrderAttachment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orderPartyOrOrderAttachment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrderPartyOrOrderAttachment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Party }
     * {@link Attachment }
     * 
     * 
     */
    public List<Object> getOrderPartyOrOrderAttachment() {
        if (orderPartyOrOrderAttachment == null) {
            orderPartyOrOrderAttachment = new ArrayList<Object>();
        }
        return this.orderPartyOrOrderAttachment;
    }

}

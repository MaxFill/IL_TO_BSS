
package ru.rt.oms;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.rt.oms package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OrderStatusNotification_QNAME = new QName("http://oms.rt.ru/", "orderStatusNotification");
    private final static QName _Fault_QNAME = new QName("http://oms.rt.ru/", "Fault");
    private final static QName _OrderStatusResponse_QNAME = new QName("http://oms.rt.ru/", "orderStatusResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.rt.oms
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OrderStatusNotification }
     * 
     */
    public OrderStatusNotification createOrderStatusNotification() {
        return new OrderStatusNotification();
    }

    /**
     * Create an instance of {@link TFault }
     * 
     */
    public TFault createTFault() {
        return new TFault();
    }

    /**
     * Create an instance of {@link NotificationResponse }
     * 
     */
    public NotificationResponse createNotificationResponse() {
        return new NotificationResponse();
    }

    /**
     * Create an instance of {@link OIReference }
     * 
     */
    public OIReference createOIReference() {
        return new OIReference();
    }

    /**
     * Create an instance of {@link OrderItemResult }
     * 
     */
    public OrderItemResult createOrderItemResult() {
        return new OrderItemResult();
    }

    /**
     * Create an instance of {@link Attribute }
     * 
     */
    public Attribute createAttribute() {
        return new Attribute();
    }

    /**
     * Create an instance of {@link Attributes }
     * 
     */
    public Attributes createAttributes() {
        return new Attributes();
    }

    /**
     * Create an instance of {@link Attachment }
     * 
     */
    public Attachment createAttachment() {
        return new Attachment();
    }

    /**
     * Create an instance of {@link InheritableAttribute }
     * 
     */
    public InheritableAttribute createInheritableAttribute() {
        return new InheritableAttribute();
    }

    /**
     * Create an instance of {@link OrderNotification }
     * 
     */
    public OrderNotification createOrderNotification() {
        return new OrderNotification();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link OrderStatus }
     * 
     */
    public OrderStatus createOrderStatus() {
        return new OrderStatus();
    }

    /**
     * Create an instance of {@link Party }
     * 
     */
    public Party createParty() {
        return new Party();
    }

    /**
     * Create an instance of {@link InheritableAttributes }
     * 
     */
    public InheritableAttributes createInheritableAttributes() {
        return new InheritableAttributes();
    }

    /**
     * Create an instance of {@link Comment }
     * 
     */
    public Comment createComment() {
        return new Comment();
    }

    /**
     * Create an instance of {@link OrderParties }
     * 
     */
    public OrderParties createOrderParties() {
        return new OrderParties();
    }

    /**
     * Create an instance of {@link OIReferences }
     * 
     */
    public OIReferences createOIReferences() {
        return new OIReferences();
    }

    /**
     * Create an instance of {@link Comments }
     * 
     */
    public Comments createComments() {
        return new Comments();
    }

    /**
     * Create an instance of {@link NotificationOrderItems }
     * 
     */
    public NotificationOrderItems createNotificationOrderItems() {
        return new NotificationOrderItems();
    }

    /**
     * Create an instance of {@link OrderNotifications }
     * 
     */
    public OrderNotifications createOrderNotifications() {
        return new OrderNotifications();
    }

    /**
     * Create an instance of {@link NotificationOrderItem }
     * 
     */
    public NotificationOrderItem createNotificationOrderItem() {
        return new NotificationOrderItem();
    }

    /**
     * Create an instance of {@link OrderItemSpecification }
     * 
     */
    public OrderItemSpecification createOrderItemSpecification() {
        return new OrderItemSpecification();
    }

    /**
     * Create an instance of {@link OrderItemBillingInfo }
     * 
     */
    public OrderItemBillingInfo createOrderItemBillingInfo() {
        return new OrderItemBillingInfo();
    }

    /**
     * Create an instance of {@link OrderResult }
     * 
     */
    public OrderResult createOrderResult() {
        return new OrderResult();
    }

    /**
     * Create an instance of {@link EquipmentInfo }
     * 
     */
    public EquipmentInfo createEquipmentInfo() {
        return new EquipmentInfo();
    }

    /**
     * Create an instance of {@link Location }
     * 
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderStatusNotification }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://oms.rt.ru/", name = "orderStatusNotification")
    public JAXBElement<OrderStatusNotification> createOrderStatusNotification(OrderStatusNotification value) {
        return new JAXBElement<OrderStatusNotification>(_OrderStatusNotification_QNAME, OrderStatusNotification.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://oms.rt.ru/", name = "Fault")
    public JAXBElement<TFault> createFault(TFault value) {
        return new JAXBElement<TFault>(_Fault_QNAME, TFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotificationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://oms.rt.ru/", name = "orderStatusResponse")
    public JAXBElement<NotificationResponse> createOrderStatusResponse(NotificationResponse value) {
        return new JAXBElement<NotificationResponse>(_OrderStatusResponse_QNAME, NotificationResponse.class, null, value);
    }

}

package spring.modulith.order.events;

import spring.modulith.order.Order.OrderIdentifier;
import org.jmolecules.event.types.DomainEvent;

public record OrderCompletedEvent(OrderIdentifier orderId) implements DomainEvent {
}

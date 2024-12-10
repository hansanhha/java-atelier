package spring.modulith.order.events;

import spring.modulith.order.Order.OrderIdentifier;

public record OrderRequestEvent(OrderIdentifier orderId) {
}

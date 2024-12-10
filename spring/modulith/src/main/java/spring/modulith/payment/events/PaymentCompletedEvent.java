package spring.modulith.payment.events;

import spring.modulith.order.Order.OrderIdentifier;
import spring.modulith.payment.entity.Payment.PaymentIdentifier;

public record PaymentCompletedEvent(PaymentIdentifier paymentId, OrderIdentifier orderId) {
}

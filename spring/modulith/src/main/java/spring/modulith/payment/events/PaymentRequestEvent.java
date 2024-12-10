package spring.modulith.payment.events;

import spring.modulith.payment.entity.Payment.PaymentIdentifier;

public record PaymentRequestEvent(PaymentIdentifier paymentId) {
}

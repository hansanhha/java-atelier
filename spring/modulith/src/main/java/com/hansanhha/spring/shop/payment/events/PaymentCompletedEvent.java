package com.hansanhha.spring.shop.payment.events;

import com.hansanhha.spring.shop.order.Order.OrderIdentifier;
import com.hansanhha.spring.shop.payment.entity.Payment.PaymentIdentifier;

public record PaymentCompletedEvent(PaymentIdentifier paymentId, OrderIdentifier orderId) {
}

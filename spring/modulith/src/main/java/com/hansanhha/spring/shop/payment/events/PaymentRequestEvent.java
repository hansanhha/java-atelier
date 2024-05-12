package com.hansanhha.spring.shop.payment.events;

import com.hansanhha.spring.shop.payment.entity.Payment.PaymentIdentifier;

public record PaymentRequestEvent(PaymentIdentifier paymentId) {
}

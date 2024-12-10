package spring.modulith.payment;

import spring.modulith.order.events.OrderRequestEvent;
import spring.modulith.payment.entity.Payment;
import spring.modulith.payment.events.PaymentCompletedEvent;
import spring.modulith.payment.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentGateway {

    private final @NonNull ApplicationEventPublisher events;
    private final @NonNull PaymentService paymentService;

    @ApplicationModuleListener
    public void startPayment(OrderRequestEvent event) {
        log.info("Received order request for {}.", event.orderId());

        Payment payment = Payment.create(event.orderId().toString(), 1000);

        ready(payment);
        approve(payment);

        log.info("succeed order request for {}.", event.orderId());

        events.publishEvent(new PaymentCompletedEvent(payment.getPaymentIdentifier(), event.orderId()));
    }

    private void ready(Payment payment) {
        paymentService.request(payment);
    }

    private void approve(Payment payment) {
        paymentService.pay(payment);
    }
}

package com.hansanhha.spring.shop.order;

import com.hansanhha.spring.shop.order.events.OrderCompletedEvent;
import com.hansanhha.spring.shop.order.events.OrderRequestEvent;
import com.hansanhha.spring.shop.order.internal.OrderInternal;
import com.hansanhha.spring.shop.payment.events.PaymentCompletedEvent;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

/*
    TODO
     - order <-> payment 순환참조 발생 (도메인 이벤트 패턴, 이벤트 중재자 패턴 사용 필요)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderManagement {

    private final @NonNull ApplicationEventPublisher events;
    private final @NonNull OrderInternal orderInternal;

    @Transactional
    public void request(Order order) {
        log.info("publish order request event for order id : {}.", order.getId());
        events.publishEvent(new OrderRequestEvent(order.getId()));
    }

//    @ApplicationModuleListener
//    @Transactional
//    public void complete(PaymentCompletedEvent event) {
//        log.info("Received payment completion for {}.", event.orderId());
//        events.publishEvent(new OrderCompletedEvent(event.orderId()));
//    }
}

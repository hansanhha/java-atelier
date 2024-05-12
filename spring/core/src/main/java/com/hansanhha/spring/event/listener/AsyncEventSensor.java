package com.hansanhha.spring.event.listener;

import com.hansanhha.spring.event.event.CircuitBreakerEvent;
import com.hansanhha.spring.event.event.CircuitBreakerEvent.CircuitBreakerEventTime;
import com.hansanhha.spring.event.event.CloseMarketEvent;
import com.hansanhha.spring.event.event.LaunchMarketEvent;
import com.hansanhha.spring.event.event.SuspendMarketEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class AsyncEventSensor {

    /**
     * 스프링 이벤트는 동기적으로 실행됨(기본값)
     * 따라서 publisher가 ApplicationEvent를 발행하면 모든 리스너가 이벤트를 끝낼 때까지 block 상태가 됨
     *
     * @Async를 통해 비동기적으로 이벤트를 처리
     * @Configuration 클래스 또는 @SpringBootApplication 클래스에 @EnableAsync 적용 필요
     * 실행 시 스레드가 별도로 생성되어 비동기적으로 이벤트를 처리함 (EventTests 테스트 코드 참고)
     */
    @Async
    @EventListener
    void launchMarket(LaunchMarketEvent event) {
        log.info("Detected event by AsyncEventSensor : LaunchMarketEvent");
    }

    @Async
    @EventListener
    void closeMarket(CloseMarketEvent event) {
        log.info("Detected event by AsyncEventSensor : CloseMarketEvent");
    }

    @Async
    @EventListener
    void suspendMarker(SuspendMarketEvent event) {
        log.info("Detected event by AsyncEventSensor : SuspendMarketEvent");
    }

    @Async
    @EventListener
    void circuitBreaker(CircuitBreakerEvent event) {
        log.info("Detected event by AsyncEventSensor : CircuitBreakerEvent");

        CircuitBreakerEventTime time = (CircuitBreakerEventTime) event.getSource();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("CircuitBreaker occurrence : {}", formatter.format(time.occurrenceDate()));
        log.info("CircuitBreaker duration : {}", formatter.format(time.duration()));
    }
}

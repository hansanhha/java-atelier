package com.hansanhha.spring.event.event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class CircuitBreakerEvent extends ApplicationEvent {

    public CircuitBreakerEvent(CircuitBreakerEventTime time) {
        super(time);
    }

    public record CircuitBreakerEventTime(LocalDateTime occurrenceDate, LocalDateTime duration) {}

    @Override
    public String toString() {
        return "ApplicationEvent: SuspendStockEvent";
    }
}

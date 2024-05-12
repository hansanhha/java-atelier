package com.hansanhha.spring.event.pulisher;

import com.hansanhha.spring.event.event.CircuitBreakerEvent;
import org.springframework.context.ApplicationEventPublisherAware;

public interface StockMarket extends ApplicationEventPublisherAware {

    void registerStocks();

    void launchMarket();

    void closeMarket();

    void circuitBreaker();

    enum MarketStatus {
        OPENED, CLOSED
    }
}

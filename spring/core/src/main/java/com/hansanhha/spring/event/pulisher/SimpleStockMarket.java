package com.hansanhha.spring.event.pulisher;

import com.hansanhha.spring.event.event.*;
import com.hansanhha.spring.event.event.CircuitBreakerEvent.CircuitBreakerEventTime;
import com.hansanhha.spring.event.listener.Kakao;
import com.hansanhha.spring.event.listener.LG;
import com.hansanhha.spring.event.listener.Samsung;
import com.hansanhha.spring.event.listener.Stock;
import com.hansanhha.spring.event.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleStockMarket implements StockMarket {

    private final StockRepository stockRepository;
    private ApplicationEventPublisher applicationEventPublisher;
    private static MarketStatus status = MarketStatus.CLOSED;

    @Transactional
    @Override
    public void registerStocks() {
        List<Stock> saved = stockRepository.saveAll(List.of(new Kakao(), new LG(), new Samsung()));

        applicationEventPublisher.publishEvent(
                new RegisterStockEvent(saved.stream().map(Stock::getId).collect(Collectors.toSet())));
    }

    @Override
    public void launchMarket() {
        applicationEventPublisher.publishEvent(new LaunchMarketEvent());
        status = MarketStatus.OPENED;
    }

    @Override
    public void closeMarket() {
        applicationEventPublisher.publishEvent(new CloseMarketEvent());
        status = MarketStatus.CLOSED;
    }

    @Override
    public void circuitBreaker() {
        applicationEventPublisher.publishEvent(new SuspendMarketEvent());

        LocalDateTime now = LocalDateTime.now();
        applicationEventPublisher.publishEvent(
                new CircuitBreakerEvent(new CircuitBreakerEventTime(now, now.plusMinutes(10))));
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}

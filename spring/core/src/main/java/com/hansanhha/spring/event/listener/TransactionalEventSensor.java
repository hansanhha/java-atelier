package com.hansanhha.spring.event.listener;

import com.hansanhha.spring.event.event.RegisterStockEvent;
import com.hansanhha.spring.event.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionalEventSensor {

    private final StockRepository stockRepository;

    @SuppressWarnings("unchecked")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void registerStock(RegisterStockEvent event) {
        log.info("Detected Event by TransactionalEventSensor : RegisterStockEvent");

        var stocks = stockRepository.findAllById((Set<Long>) event.getSource());

        stocks.forEach(stock -> {
            stock.log("Registered");
        });
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void registerStockBeforeCommit(RegisterStockEvent event) {
        log.info("Detected Event by TransactionalEventSensor : RegisterStockEvent Before Commit");
        log.info("event.getSource() : {}", event.getSource());
    }
}

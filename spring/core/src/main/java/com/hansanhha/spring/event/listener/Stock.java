package com.hansanhha.spring.event.listener;

import com.hansanhha.spring.event.event.SuspendMarketEvent;
import com.hansanhha.spring.event.event.CloseMarketEvent;
import com.hansanhha.spring.event.event.LaunchMarketEvent;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Entity
@Inheritance
@DiscriminatorColumn(name="stock_name")
@Getter
public abstract class Stock {

    protected Stock() {
        status = TransactionStatus.INACTIVE;
    }

    @Id
    @GeneratedValue
    protected Long id;

    protected int price;

    protected TransactionStatus status;

    void increase(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("price should be positive");
        }
        this.price += price;
    }

    void decrease(int price) {
        if (price < 0 || this.price - price < 0) {
            throw new IllegalArgumentException("price should be positive");
        }
        this.price -= price;
    }

    @EventListener
    void active(LaunchMarketEvent event) {
        setTransactionStatus(TransactionStatus.ACTIVE);
        log("active");
    }

    @EventListener
    void inactive(CloseMarketEvent event) {
        setTransactionStatus(TransactionStatus.INACTIVE);
        log("stop");
    }

    @EventListener
    void suspend(SuspendMarketEvent event) {
        setTransactionStatus(TransactionStatus.SUSPEND);
        log("suspend");
    }

    void setTransactionStatus(TransactionStatus status) {
        this.status = status;
    }

    TransactionStatus getTransactionStatus() {
        return status;
    }

    abstract void log(String append);

    private enum TransactionStatus {
        ACTIVE, INACTIVE, SUSPEND
    }
}

package com.hansanhha.spring.event.listener;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;


@Entity
@DiscriminatorValue("Samsung")
@Slf4j
public class Samsung extends Stock {

    public Samsung() {
        super();
        price = 3000;
    }

    @Override
    void log(String append) {
        log.info("Samsung: {}", append);
    }
}

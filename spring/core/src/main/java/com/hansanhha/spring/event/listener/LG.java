package com.hansanhha.spring.event.listener;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;

@Entity
@DiscriminatorValue("LG")
@Slf4j
public class LG extends Stock{

    public LG() {
        super();
        price = 2000;
    }

    @Override
    void log(String append) {
        log.info("LG: {}", append);
    }
}

package com.hansanhha.spring.event.listener;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;


@Entity
@DiscriminatorValue("Kakao")
@Slf4j
public class Kakao extends Stock{

    public Kakao() {
        super();
        price = 1000;
    }

    @Override
    void log(String append) {
        log.info("Kakao: {}", append);
    }
}

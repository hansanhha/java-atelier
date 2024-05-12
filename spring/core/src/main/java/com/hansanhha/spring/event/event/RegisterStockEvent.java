package com.hansanhha.spring.event.event;

import com.hansanhha.spring.event.listener.Kakao;
import com.hansanhha.spring.event.listener.LG;
import com.hansanhha.spring.event.listener.Samsung;
import com.hansanhha.spring.event.listener.Stock;
import org.springframework.context.ApplicationEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegisterStockEvent extends ApplicationEvent {

    public RegisterStockEvent(Set<Long> stocks) {
        super(stocks);
    }
}

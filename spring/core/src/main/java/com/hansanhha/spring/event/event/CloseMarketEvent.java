package com.hansanhha.spring.event.event;

import org.springframework.context.ApplicationEvent;

public class CloseMarketEvent extends ApplicationEvent {

    public CloseMarketEvent() {
        super("Close Market Event");
    }

    @Override
    public String toString() {
        return "ApplicationEvent: CloseMarketEvent";
    }
}

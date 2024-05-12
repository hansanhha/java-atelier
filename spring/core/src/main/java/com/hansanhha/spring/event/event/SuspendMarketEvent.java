package com.hansanhha.spring.event.event;

import org.springframework.context.ApplicationEvent;

public class SuspendMarketEvent extends ApplicationEvent {

    public SuspendMarketEvent() {
        super("Suspend Market Event");
    }

    @Override
    public String toString() {
        return "ApplicationEvent: SuspendMarketEvent";
    }
}

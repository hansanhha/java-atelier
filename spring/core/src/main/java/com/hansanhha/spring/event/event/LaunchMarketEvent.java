package com.hansanhha.spring.event.event;

import org.springframework.context.ApplicationEvent;

public class LaunchMarketEvent extends ApplicationEvent {

    public LaunchMarketEvent() {
        super("Launch Market Event");
    }

    @Override
    public String toString() {
        return "ApplicationEvent: LaunchMarketEvent";
    }
}

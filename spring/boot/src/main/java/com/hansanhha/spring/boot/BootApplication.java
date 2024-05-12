package com.hansanhha.spring.boot;

import com.hansanhha.spring.boot.event.listener.SpringApplicationListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BootApplication.class);
        app.addListeners(new SpringApplicationListener());
        app.run(args);
    }
}

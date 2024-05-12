package com.hansanhha.spring.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanyConfig {

    @Bean
    public Samsung samsung() {
        return new Samsung();
    }

    @Bean
    public TSMC tsmc() {
        return new TSMC();
    }

    private static class Samsung {

    }

    private static class TSMC {

    }
}

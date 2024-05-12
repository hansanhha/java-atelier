package com.hansanhha.spring.shop.inventory;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("shop.inventory")
record InventorySettings(int stockThreshold) {

    @Override
    public int stockThreshold() {
        return stockThreshold;
    }
}

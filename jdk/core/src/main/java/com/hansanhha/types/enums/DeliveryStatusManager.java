package com.hansanhha.types.enums;

import java.time.LocalDateTime;

public enum DeliveryStatusManager implements StatusManager<DeliveryStatusManager> {

    PREPARING("준비 중") {
        @Override
        public DeliveryStatusManager nextStatus(DeliveryStatusManager currentStatus) {
            READY.time.set(LocalDateTime.now());
            return READY;
        }
    },
    READY("준비됨") {
        @Override
        public DeliveryStatusManager nextStatus(DeliveryStatusManager currentStatus) {
            READY.time.set(LocalDateTime.now());
            return DELIVERING;
        }
    },

    DELIVERING("배송 중") {
        @Override
        public DeliveryStatusManager nextStatus(DeliveryStatusManager currentStatus) {
            DELIVERED.time.set(LocalDateTime.now());
            return DELIVERED;
        }
    },

    DELIVERED("배송됨") {
        @Override
        public DeliveryStatusManager nextStatus(DeliveryStatusManager currentStatus) {
            throw new IllegalCallerException();
        }
    };

    private final String value;
    private ThreadLocal<LocalDateTime> time;

    DeliveryStatusManager(String value) {
        this.value = value;
        time.set(LocalDateTime.now());
    }

    public String getValue() {
        return value;
    }
}

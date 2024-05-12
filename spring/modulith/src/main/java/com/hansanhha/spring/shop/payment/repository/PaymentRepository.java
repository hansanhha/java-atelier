package com.hansanhha.spring.shop.payment.repository;

import com.hansanhha.spring.shop.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

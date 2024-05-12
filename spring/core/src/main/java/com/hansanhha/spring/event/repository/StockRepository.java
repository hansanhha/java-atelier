package com.hansanhha.spring.event.repository;

import com.hansanhha.spring.event.listener.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}

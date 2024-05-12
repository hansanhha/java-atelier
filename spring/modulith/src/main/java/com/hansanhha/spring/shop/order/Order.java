package com.hansanhha.spring.shop.order;

import com.hansanhha.spring.shop.order.Order.OrderIdentifier;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public class Order implements AggregateRoot<Order, OrderIdentifier> {

    private final OrderIdentifier id = new OrderIdentifier(UUID.randomUUID());

    public static record OrderIdentifier(UUID id) implements Identifier {}

    @Override
    public OrderIdentifier getId() {
        return id;
    }
}

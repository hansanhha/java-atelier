package mockito.springboot.order;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class OrderRepositoryImpl implements OrderRepository{

    private final HashMap<String, Order> orderMap = new HashMap<>();

    @Override
    public Order findById(String id) {
        return orderMap.get(id);
    }

    @Override
    public Order saveOrder(Order order) {
        orderMap.put(order.getId(), order);
        return order;
    }

    @Override
    public Order updateOrder(Order order) {
        orderMap.get(order.getId()).setOrderStatus(order.getOrderStatus());
        return order;
    }
}

package mockito.order;

import mockito.user.User;
import mockito.user.UserRepository;

public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public Order getOrder(String id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        User user = userRepository.findById(order.getUserId());
        if (user != null) {
            return orderRepository.saveOrder(order);
        }
        return null;
    }

    public Order updateOrderStatus(String orderId, OrderStatus order) {
        var found = orderRepository.findById(orderId);

        if (!found.isChangeableOrderStatus()) {
            throw new IllegalArgumentException("주문 상태 변경이 불가능합니다.");
        }

        found.setOrderStatus(order);
        return orderRepository.updateOrder(found);
    }
}

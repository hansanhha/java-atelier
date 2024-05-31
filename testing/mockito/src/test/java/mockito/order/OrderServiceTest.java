package mockito.order;

import mockito.user.User;
import mockito.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 생성")
    @Test
    void saveOrder() {
        var order = createOrder();

        doReturn(createUser())
                .when(userRepository)
                .findById(anyString());

        doReturn(order)
                .when(orderRepository)
                .saveOrder(any(Order.class));

        var ordered = orderService.saveOrder(order);

        verify(userRepository, times(1)).findById(anyString());
        verify(orderRepository, times(1)).saveOrder(any(Order.class));

        assertNotNull(ordered);
        assertEquals(order.getId(), ordered.getId());
    }

    @DisplayName("주문 조회")
    @Test
    void getOrder() {
        var order = createOrder();

        doReturn(order)
                .when(orderRepository)
                .findById(anyString());

        Order found = orderService.getOrder(order.getId());

        verify(orderRepository, times(1)).findById(anyString());

        assertNotNull(found);
        assertEquals(order.getId(), found.getId());
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void SuccessUpdateOrderStatus() {
        var order = createOrder();

        doReturn(order)
                .when(orderRepository)
                .findById(anyString());

        orderService.updateOrderStatus(order.getId(), OrderStatus.COMPLETED);

        verify(orderRepository, times(1)).findById(anyString());
        verify(orderRepository, times(1)).updateOrder(any(Order.class));
    }

    @DisplayName("주문 상태 변경 실패")
    @Test
    void FailUpdateOrderStatus() {
        var canceledOrder = createCancelStatusOrder();

        doReturn(canceledOrder)
                .when(orderRepository)
                .findById(any(String.class));

        assertThrows(IllegalArgumentException.class, () ->
            orderService.updateOrderStatus(canceledOrder.getId(), OrderStatus.COMPLETED)
        );

        verify(orderRepository, times(1)).findById(any(String.class));
        verify(orderRepository, times(0)).updateOrder(any(Order.class));
    }

    private Order createOrder() {
        User user = createUser();
        return new Order("1", user.getId(), "item", 100, OrderStatus.ORDERED);
    }

    private Order createCancelStatusOrder() {
        return new Order("2", null, "item", 100, OrderStatus.CANCELED);
    }

    private User createUser() {
        return new User("1", "unit man");
    }
}
package mockito.order;

public interface OrderRepository {

    Order findById(String id);
    Order saveOrder(Order order);
    Order updateOrder(Order order);
}

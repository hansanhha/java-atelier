package mockito.order;

import java.util.Objects;

public class Order {

    private final String id;
    private final String userId;
    private final String product;
    private final int quantity;
    private OrderStatus orderStatus;

    public Order(String id, String userId, String product, int quantity, OrderStatus orderStatus) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
    }

    public boolean isChangeableOrderStatus() {
        return Objects.requireNonNull(orderStatus).equals(OrderStatus.ORDERED);
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

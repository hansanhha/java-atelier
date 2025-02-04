package hansanhha;

import java.util.List;

public class OrderProcessor {

    public double calculateTotalPrice(List<Double> itemPrices, double discount, double taxRate) {
        if (itemPrices == null || itemPrices.isEmpty()) {
            throw new IllegalArgumentException("Item prices cannot be empty");
        }

        double subtotal = 0;
        for (Double price : itemPrices) {
            subtotal += price;
        }

        double discountAmount = subtotal * discount;
        double taxAmount = (subtotal - discountAmount) * taxRate;
        return subtotal - discountAmount + taxAmount;
    }
}

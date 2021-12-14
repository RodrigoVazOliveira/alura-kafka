package dev.rvz.models.serializables;

import java.math.BigDecimal;

public class Order {

    private final String userId;
    private final String OrderId;
    private final BigDecimal ammount;

    public Order(String userId, String orderId, BigDecimal ammount) {
        this.userId = userId;
        OrderId = orderId;
        this.ammount = ammount;
    }

    public String getUserId() {
        return userId;
    }
}

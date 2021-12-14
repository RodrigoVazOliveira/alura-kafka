package dev.rvz.models.serializables;

import java.math.BigDecimal;

public class Order {

    private final String userId;
    private final String OrderId;
    private final BigDecimal ammount;
    private final String email;

    public Order(String userId, String orderId, BigDecimal ammount, String email) {
        this.userId = userId;
        OrderId = orderId;
        this.ammount = ammount;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }
}

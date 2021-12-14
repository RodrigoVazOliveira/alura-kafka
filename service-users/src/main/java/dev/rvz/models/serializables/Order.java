package dev.rvz.models.serializables;

import com.google.gson.Gson;

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

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

package dev.rvz.models.serializables;

import com.google.gson.Gson;

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

    public String getOrderId() {
        return OrderId;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

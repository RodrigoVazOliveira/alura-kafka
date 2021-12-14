package dev.rvz.models.serializables;

import com.google.gson.Gson;

import java.math.BigDecimal;

public class Order {

    private final String OrderId;
    private final BigDecimal ammount;
    private final String email;

    public Order(String orderId, BigDecimal ammount, String email) {
        OrderId = orderId;
        this.ammount = ammount;
        this.email = email;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

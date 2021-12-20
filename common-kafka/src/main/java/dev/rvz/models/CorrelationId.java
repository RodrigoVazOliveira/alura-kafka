package dev.rvz.models;

import com.google.gson.Gson;

import java.util.UUID;

public class CorrelationId {
    private final String id;

    public CorrelationId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

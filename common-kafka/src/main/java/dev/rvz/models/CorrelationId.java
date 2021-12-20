package dev.rvz.models;

import com.google.gson.Gson;

import java.util.UUID;

public class CorrelationId {
    private final String id;

    public CorrelationId(String title) {
        this.id = title + "(" + UUID.randomUUID().toString() + ")";
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public CorrelationId continueWith(String title) {
        return new CorrelationId(id + "-" + title);
    }
}

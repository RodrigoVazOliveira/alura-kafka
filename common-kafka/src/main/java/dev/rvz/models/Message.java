package dev.rvz.models;

import com.google.gson.Gson;

public class Message<T> {

    private final CorrelationId id;
    private final T payload;

    Message(CorrelationId id, T payload) {
        this.id = id;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

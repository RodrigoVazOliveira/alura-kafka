package dev.rvz.models;

import java.util.UUID;

public class CorrelationId {
    private final String id;

    public CorrelationId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}

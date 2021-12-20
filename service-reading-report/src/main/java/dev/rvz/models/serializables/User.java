package dev.rvz.models.serializables;

public class User {
    private final String uuid;

    public User(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPath() {
        return "target/" + uuid + "-report.txt";
    }
}

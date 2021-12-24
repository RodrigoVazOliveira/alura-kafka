package dev.rvz.models.deserializers;

import com.google.gson.Gson;

public class Email {
    private final String subject;
    private final String body;

    public Email(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    public String getBody() {
        return new String(body);
    }



    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

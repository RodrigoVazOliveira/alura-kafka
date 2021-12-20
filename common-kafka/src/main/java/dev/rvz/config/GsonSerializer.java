package dev.rvz.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.rvz.models.Message;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();

    @Override
    public byte[] serialize(String s, T object) {
        return gson.toJson(object).getBytes(StandardCharsets.UTF_8);
    }
}

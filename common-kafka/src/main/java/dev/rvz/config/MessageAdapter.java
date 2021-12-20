package dev.rvz.config;

import com.google.gson.*;
import dev.rvz.models.CorrelationId;
import dev.rvz.models.Message;

import java.lang.reflect.Type;

public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {
    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", message.getPayload().getClass().getName());
        jsonObject.add("payload", context.serialize(message.getPayload()));
        jsonObject.add("correlationId", context.serialize(message.getId()));

        return jsonObject;
    }

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
       JsonObject jsonObject = jsonElement.getAsJsonObject();
        String typePayload = jsonObject.get("type").getAsString();
        CorrelationId correlationId = jsonDeserializationContext.deserialize(jsonObject.get("correlationId"), CorrelationId.class);
        try {
            // maybe you want to use an accent list
            Object payload = jsonDeserializationContext.deserialize(jsonObject.get("payload"), Class.forName(typePayload));
            return new Message<>(correlationId, payload);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}

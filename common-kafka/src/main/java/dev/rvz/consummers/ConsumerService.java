package dev.rvz.consummers;

import dev.rvz.models.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public interface ConsumerService<T> {

    String getTopic();
    String getGroupId();
    void parse(ConsumerRecord<String, Message<T>> record) throws IOException;
}
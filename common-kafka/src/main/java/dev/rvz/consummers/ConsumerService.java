package dev.rvz.consummers;

import dev.rvz.models.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService<T> {

    String getTopic();
    String getGroupId();

    void parse(ConsumerRecord<String, Message<String>> record);
}
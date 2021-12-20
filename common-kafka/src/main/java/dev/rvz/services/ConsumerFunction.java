package dev.rvz.services;

import dev.rvz.models.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFunction<T> {
    void consumer(ConsumerRecord<String, Message<T>> consumerRecord) throws Exception;
}

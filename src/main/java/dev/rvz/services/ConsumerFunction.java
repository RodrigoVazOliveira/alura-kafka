package dev.rvz.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFunction {
    void consumer(ConsumerRecord<String, String> consumerRecord);
}

package dev.rvz.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

public interface ConsumerFunction<T> {
    void consumer(ConsumerRecord<String, T> consumerRecord) throws ExecutionException, InterruptedException;
}

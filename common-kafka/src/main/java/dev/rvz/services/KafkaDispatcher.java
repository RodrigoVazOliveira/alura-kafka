package dev.rvz.services;

import dev.rvz.config.GsonSerializer;
import dev.rvz.models.CorrelationId;
import dev.rvz.models.Message;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, Message<T>> kafkaProducer;

    public KafkaDispatcher() {
        this.kafkaProducer = new KafkaProducer<>(properties());
    }

    public void sendMessage(String topic, String key, CorrelationId id, T payload) throws ExecutionException, InterruptedException {
        producerSend(topic, key, id, payload).get();
    }

    public void sendMessageAsync(String topic, String key, CorrelationId id, T payload) {
        producerSend(topic, key, id, payload);
    }

    @Override
    public void close() {
        this.kafkaProducer.close();
    }

    private Future<RecordMetadata> producerSend(String topic, String key, CorrelationId id, T payload) {
        Message<T> message = new Message<>(id, payload);

        ProducerRecord<String, Message<T>> producerRecord = new ProducerRecord<>(topic, key, message);

        return kafkaProducer.send(producerRecord, (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
        });
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");

        return properties;
    }
}
package dev.rvz.services;

import dev.rvz.config.GsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, T> kafkaProducer;

    public KafkaDispatcher() {
        this.kafkaProducer = new KafkaProducer<>(properties());
    }

    public void sendMessage(String topic, String key, T value) throws ExecutionException, InterruptedException {
        ProducerRecord<String, T> producerRecord = new ProducerRecord<>(topic, key, value);

        kafkaProducer.send(producerRecord, (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
        }).get();
    }

    @Override
    public void close() {
        this.kafkaProducer.close();
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
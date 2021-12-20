package dev.rvz.services;

import dev.rvz.config.GsonDeserializer;
import dev.rvz.models.Message;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {

    private final KafkaConsumer<String, Message<T>> kafkaConsumer;
    private final ConsumerFunction<T> consumerFunction;
    private final String groupId;
    private final Map<String, String> properties;

    public KafkaService(String groupId, String topic, ConsumerFunction<T> parse, Map<String, String> properties) {
        this.groupId = groupId;
        this.properties = properties;
        this.kafkaConsumer = new KafkaConsumer<>(getProperties());
        this.kafkaConsumer.subscribe(Collections.singletonList(topic));
        this.consumerFunction = parse;
    }

    public KafkaService(String groupId, Pattern topic, ConsumerFunction<T> parse, Map<String, String> properties) {
        this.groupId = groupId;
        this.properties = properties;
        this.kafkaConsumer = new KafkaConsumer<>(getProperties());
        this.kafkaConsumer.subscribe(topic);
        this.consumerFunction = parse;
    }

    public void run() {
        waitMessage(this.kafkaConsumer);
    }

    private void waitMessage(KafkaConsumer<String, Message<T>> kafkaConsumer) {
        while (true) {
            ConsumerRecords<String, Message<T>> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
            verifyRecordsNotEmpty(consumerRecords);
        }
    }

    private void verifyRecordsNotEmpty(ConsumerRecords<String, Message<T>> consumerRecords) {
        if (!consumerRecords.isEmpty()) {
            showMessages(consumerRecords);
        }
    }

    private void showMessages(ConsumerRecords<String, Message<T>> consumerRecords) {
        for (ConsumerRecord<String, Message<T>> consumerRecord : consumerRecords) {
            try {
                this.consumerFunction.consumer(consumerRecord);
            } catch (Exception e) {
                // only catches exceptions because no mattter which Exceptions
                // i want recover and parse the next one
                // so far, just logging the exceptions for this message
                e.printStackTrace();
            }
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.putAll(this.properties);

        return properties;
    }

    @Override
    public void close() {
        this.kafkaConsumer.close();
    }
}

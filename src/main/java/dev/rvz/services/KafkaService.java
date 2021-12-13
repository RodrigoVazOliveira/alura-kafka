package dev.rvz.services;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class KafkaService implements Closeable {

    private final KafkaConsumer<String, String> kafkaConsumer;
    private final ConsumerFunction consumerFunction;
    private final String groupId;

    public KafkaService(String groupId, String topic, ConsumerFunction parse) {
        this.groupId = groupId;
        this.kafkaConsumer = new KafkaConsumer<>(properties());
        this.kafkaConsumer.subscribe(Collections.singletonList(topic));
        this.consumerFunction = parse;
    }

    public void run() {
        waitMessage(this.kafkaConsumer);
    }

    private void waitMessage(KafkaConsumer<String, String> kafkaConsumer) {
        while (true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
            verifyRecordsNotEmpty(consumerRecords);
        }
    }

    private void verifyRecordsNotEmpty(ConsumerRecords<String, String> consumerRecords) {
        if (!consumerRecords.isEmpty()) {
            showMessages(consumerRecords);
        }
    }

    private void showMessages(ConsumerRecords<String, String> consumerRecords) {
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            this.consumerFunction.consumer(consumerRecord);
        }
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");

        return properties;
    }

    @Override
    public void close() {
        this.kafkaConsumer.close();
    }
}

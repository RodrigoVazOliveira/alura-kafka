package dev.rvz.consummers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties());
        kafkaConsumer.subscribe(Pattern.compile("ECOMMERCE.*"));
        getMessagesLog(kafkaConsumer);
    }

    private static void getMessagesLog(KafkaConsumer<String, String> kafkaConsumer) {
        while (true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
            verifyConsumerNotEmpty(consumerRecords);
        }
    }

    private static void verifyConsumerNotEmpty(ConsumerRecords<String, String> consumerRecords) {
        if (!consumerRecords.isEmpty()) {
            showMessages(consumerRecords);
        }
    }

    private static void showMessages(ConsumerRecords<String, String> consumerRecords) {
        consumerRecords.forEach((message) -> {
            System.out.printf("topic: %s\n", message.topic());
            System.out.printf("chave: %s, valor: %s, partição: %s, offset: %s\n", message.key(), message.value(), message.partition(), message.offset());
        });
    }

    private static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");

        return properties;
    }
}

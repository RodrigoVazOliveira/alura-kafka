package dev.rvz;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class EmailService {
    public static void main(String[] args) {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties());
        kafkaConsumer.subscribe(Collections.singletonList("ECOMMERCE_SEND_MAIL"));
        waitMessage(kafkaConsumer);
    }

    private  static void waitMessage(KafkaConsumer<String, String> kafkaConsumer) {
        while (true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
            verifyRecordsNotEmpty(consumerRecords);
        }
    }

    private static void verifyRecordsNotEmpty(ConsumerRecords<String, String> consumerRecords) {
        if (!consumerRecords.isEmpty()) {
            System.out.println("Registros encontrado!");
            showMessages(consumerRecords);
        }
    }

    private static void showMessages(ConsumerRecords<String, String> consumerRecords) {
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            System.out.println("----------------------------- EMAIL ---------------------------------");
            System.out.println("chave: " + consumerRecord.key());
            System.out.println("valor: " + consumerRecord.value());
            System.out.println("partição: " + consumerRecord.partition());
            System.out.println("offset: " + consumerRecord.offset());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, EmailService.class.getSimpleName());

        return properties;
    }
}

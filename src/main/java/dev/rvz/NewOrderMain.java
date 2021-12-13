package dev.rvz;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties());

        for (Integer i = 0; i < 10; i++) {
            sendEmailToTopicMail(kafkaProducer);
            sendNewOrder(kafkaProducer);
        }
    }

    private static void sendNewOrder(KafkaProducer<String, String> kafkaProducer) throws ExecutionException, InterruptedException {
        String key = UUID.randomUUID().toString();
        String values = key + ", 45, 6, 10";
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", values, values);


        kafkaProducer.send(producerRecord, (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }

            System.out.println("topic: " + data.topic() + ":::partition " + data.partition() + "/ offset:" +  data.offset() + "/ timestamp: " + data.timestamp());
        }).get();
    }

    private static void sendEmailToTopicMail(KafkaProducer<String, String> kafkaProducer) throws ExecutionException, InterruptedException {
        String email = "Thank you for your order! We are processing your order!";
        ProducerRecord<String, String> emailProducer = new ProducerRecord<>("ECOMMERCE_SEND_MAIL", UUID.randomUUID().toString(), email);

        kafkaProducer.send(emailProducer, (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
            }
        }).get();
    }

    public static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }
}

package dev.rvz;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties());
        String values = "123213123, 1312313, 1312313";
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", values, values);


        kafkaProducer.send(producerRecord, (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }

            System.out.println("topic: " + data.topic() + ":::partition " + data.partition() + "/ offset:" +  data.offset() + "/ timestamp: " + data.timestamp());
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

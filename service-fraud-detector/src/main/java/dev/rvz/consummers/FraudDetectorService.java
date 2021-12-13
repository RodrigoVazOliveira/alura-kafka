package dev.rvz.consummers;

import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.HashMap;

public class FraudDetectorService {
    public static void main(String[] args) {
        FraudDetectorService fraudDetectorService = new FraudDetectorService();
        KafkaService kafkaService = new KafkaService(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudDetectorService::parse, Order.class, new HashMap<>());
        kafkaService.run();
    }

    void parse(ConsumerRecord<String, Order> consumerRecord) {
        System.out.printf("topic: %s\n", consumerRecord.topic());
        System.out.printf("chave: %s, valor: %s, partição: %s, offset: %s\n", consumerRecord.key(), consumerRecord.value(),
                consumerRecord.partition(), consumerRecord.offset());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

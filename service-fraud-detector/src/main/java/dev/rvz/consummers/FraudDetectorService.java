package dev.rvz.consummers;

import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaDispatcher;
import dev.rvz.services.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    private final KafkaDispatcher<Order> kafkaDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) {
        FraudDetectorService fraudDetectorService = new FraudDetectorService();
        KafkaService kafkaService = new KafkaService(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudDetectorService::parse, Order.class, new HashMap<>());
        kafkaService.run();
    }

    void parse(ConsumerRecord<String, Order> consumerRecord) throws ExecutionException, InterruptedException {
        System.out.printf("topic: %s\n", consumerRecord.topic());
        System.out.printf("chave: %s, valor: %s, partição: %s, offset: %s\n", consumerRecord.key(), consumerRecord.value(),
                consumerRecord.partition(), consumerRecord.offset());

        Order order = consumerRecord.value();
        if (isaFraud(order)) {
            kafkaDispatcher.sendMessage("ECOMMERCE_ORDER_REJECTED", order.getUserId(), order);
        } else {
            kafkaDispatcher.sendMessage("ECOMMERCE_ORDER_APPROVED", order.getUserId(), order);
        }
    }

    private boolean isaFraud(Order order) {
        return order.getAmmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}

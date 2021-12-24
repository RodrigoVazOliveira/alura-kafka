package dev.rvz.producers;

import dev.rvz.models.CorrelationId;
import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaDispatcher;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaDispatcher<Order> kafkaDispatcherOrder = new KafkaDispatcher<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                    BigDecimal.valueOf(Math.random() * 5000 + 1), Math.random() + "@mail.com");
            kafkaDispatcherOrder.sendMessage("ECOMMERCE_NEW_ORDER", order.getEmail(),
                    new CorrelationId(NewOrderMain.class.getSimpleName()),order);
        }
    }
}

package dev.rvz.producers;

import dev.rvz.models.deserializers.Email;
import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaDispatcher;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaDispatcher<Order> kafkaDispatcherOrder = new KafkaDispatcher<>();
        KafkaDispatcher<Email> kafkaDispatcherEmail = new KafkaDispatcher<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                    BigDecimal.valueOf(Math.random() * 5000 + 1), Math.random() + "@mail.com");
            Email email = new Email(UUID.randomUUID().toString(), "Thank you, new order processing success!");
            kafkaDispatcherEmail.sendMessage("ECOMMERCE_SEND_MAIL", UUID.randomUUID().toString(), email);
            kafkaDispatcherOrder.sendMessage("ECOMMERCE_NEW_ORDER", order.getUserId(), order);
        }
    }
}

package dev.rvz;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaDispatcher kafkaDispatcher = new KafkaDispatcher();
        for (Integer i = 0; i < 10; i++) {
            kafkaDispatcher.sendMessage("ECOMMERCE_SEND_MAIL", UUID.randomUUID().toString(), "Thank you, new order processing success!");
            kafkaDispatcher.sendMessage("ECOMMERCE_NEW_ORDER", UUID.randomUUID().toString(), "12321321");
        }
    }
}

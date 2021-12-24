package dev.rvz;

import dev.rvz.models.CorrelationId;
import dev.rvz.models.Message;
import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaDispatcher;
import dev.rvz.services.KafkaService;
import dev.rvz.services.helpers.KafkaServiceBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ServiceEmailNewOrderApplication {

    private final KafkaDispatcher<String> sendMailKafkaDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ServiceEmailNewOrderApplication serviceEmailNewOrderApplication = new ServiceEmailNewOrderApplication();
        KafkaService kafkaService = new KafkaServiceBuilder<>().setGroupId(ServiceEmailNewOrderApplication.class.getSimpleName())
                .setProperties(new HashMap<>())
                .setParse(serviceEmailNewOrderApplication::parse)
                .setTopic("ECOMMERCE_NEW_ORDER")
                .build();
        kafkaService.run();
    }

    private void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("Processing new order, preparing email!");
        Order order = record.value().getPayload();
        CorrelationId id = record.value().getId().continueWith(ServiceEmailNewOrderApplication.class.getSimpleName());
        String emailCode = "Thank you, new order processing success!";
        sendMailKafkaDispatcher.sendMessage("ECOMMERCE_SEND_MAIL", order.getEmail(), id, emailCode);
    }

}

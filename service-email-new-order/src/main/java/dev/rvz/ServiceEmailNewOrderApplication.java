package dev.rvz;

import dev.rvz.consummers.ConsumerService;
import dev.rvz.consummers.ServiceRunner;
import dev.rvz.models.CorrelationId;
import dev.rvz.models.Message;
import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

public class ServiceEmailNewOrderApplication implements ConsumerService<Order> {

    private final KafkaDispatcher<String> sendMailKafkaDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) {
        new ServiceRunner<>(ServiceEmailNewOrderApplication::new).start(1);
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getGroupId() {
        return ServiceEmailNewOrderApplication.class.getSimpleName();
    }

    @Override
    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("Processing new order, preparing email!");
        Order order = record.value().getPayload();
        CorrelationId id = record.value().getId().continueWith(ServiceEmailNewOrderApplication.class.getSimpleName());
        String emailCode = "Thank you, new order processing success!";
        sendMailKafkaDispatcher.sendMessage("ECOMMERCE_SEND_MAIL", order.getEmail(), id, emailCode);
    }
}
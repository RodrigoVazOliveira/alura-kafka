package dev.rvz.consummers;

import dev.rvz.models.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService implements ConsumerService<String> {
    public static void main(String[] args) {
        new ServiceRunner<>(EmailService::new).start(5);
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_SEND_MAIL";
    }

    @Override
    public String getGroupId() {
        return EmailService.class.getSimpleName();
    }

    @Override
    public void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("----------------------------- EMAIL ---------------------------------");
        System.out.println("chave: " + record.key());
        System.out.println("valor: " + record.value());
        System.out.println("partição: " + record.partition());
        System.out.println("offset: " + record.offset());
    }
}

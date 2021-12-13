package dev.rvz;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {
    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        KafkaService kafkaService = new KafkaService(EmailService.class.getSimpleName(), "ECOMMERCE_SEND_MAIL",
                emailService::parse);
        kafkaService.run();
    }

    private void parse(ConsumerRecord<String, String> consumerRecord) {
        System.out.println("----------------------------- EMAIL ---------------------------------");
        System.out.println("chave: " + consumerRecord.key());
        System.out.println("valor: " + consumerRecord.value());
        System.out.println("partição: " + consumerRecord.partition());
        System.out.println("offset: " + consumerRecord.offset());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

package dev.rvz;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {
    public static void main(String[] args) {
        FraudDetectorService fraudDetectorService = new FraudDetectorService();
        KafkaService kafkaService = new KafkaService(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudDetectorService::parse);
        kafkaService.run();
    }

    void parse(ConsumerRecord<String, String> consumerRecord) {
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

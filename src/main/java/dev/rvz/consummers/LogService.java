package dev.rvz.consummers;

import dev.rvz.services.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {
        LogService logService = new LogService();
        KafkaService kafkaService = new KafkaService( LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"), logService::parse, String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()));
        kafkaService.run();
    }

    public void parse(ConsumerRecord<String, Throwable> consumerRecord) {
        System.out.printf("topic: %s\n", consumerRecord.topic());
        System.out.printf("chave: %s, valor: %s, partição: %s, offset: %s\n", consumerRecord.key(), consumerRecord.value(),
                consumerRecord.partition(), consumerRecord.offset());
    }
}

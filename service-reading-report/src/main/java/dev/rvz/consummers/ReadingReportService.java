package dev.rvz.consummers;

import dev.rvz.io.IO;
import dev.rvz.models.Message;
import dev.rvz.models.serializables.User;
import dev.rvz.services.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class ReadingReportService {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) {
        ReadingReportService readingReportService = new ReadingReportService();
        KafkaService<User> kafkaService = new KafkaService<>(ReadingReportService.class.getSimpleName(), "ECOMMERCE_USER_GENERATE_READING_REPORT",
                readingReportService::parse, new HashMap<>());
        kafkaService.run();
    }

    void parse(ConsumerRecord<String, Message<User>> consumerRecord) throws IOException {
        System.out.println("Processing report for " + consumerRecord.value());

        File target = new File(consumerRecord.value().getPayload().getPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + consumerRecord.value().getPayload().getUuid());

        System.out.println("File created: " + target.getAbsolutePath());
    }
}

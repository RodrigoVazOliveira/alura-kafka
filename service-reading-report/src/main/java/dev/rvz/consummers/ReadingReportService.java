package dev.rvz.consummers;

import dev.rvz.io.IO;
import dev.rvz.models.Message;
import dev.rvz.models.serializables.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ReadingReportService implements ConsumerService<User> {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    public static void main(String[] args) {
        new ServiceRunner<>(ReadingReportService::new).start(5);
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_USER_GENERATE_READING_REPORT";
    }

    @Override
    public String getGroupId() {
        return ReadingReportService.class.getSimpleName();
    }

    @Override
    public void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
        System.out.println("Processing report for " + record.value());

        File target = new File(record.value().getPayload().getPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + record.value().getPayload().getUuid());

        System.out.println("File created: " + target.getAbsolutePath());
    }
}

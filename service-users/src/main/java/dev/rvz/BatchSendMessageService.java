package dev.rvz;

import dev.rvz.models.Message;
import dev.rvz.models.serializables.User;
import dev.rvz.services.KafkaDispatcher;
import dev.rvz.services.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {
    private final Connection connection;
    private final KafkaDispatcher<User> userKafkaDispatcher = new KafkaDispatcher<>();

    public BatchSendMessageService() throws SQLException {
        String url = "jdbc:sqlite:target/users_data.db";
        this.connection = DriverManager.getConnection(url);
    }

    public static void main(String[] args) throws SQLException {
        BatchSendMessageService batchSendMessageService = new BatchSendMessageService();
        KafkaService<String> kafkaService = new KafkaService<>(
                BatchSendMessageService.class.getSimpleName(), "SEND_MESSAGE_TO_ALL_USERS",
                batchSendMessageService::parse,
                new HashMap<>());
        kafkaService.run();
    }

    private void parse(ConsumerRecord<String, Message<String>> record) throws SQLException, ExecutionException, InterruptedException {
        System.out.println("Processinng new batch!");
        for (User user : getAllUsers()) {
            userKafkaDispatcher.sendMessage(record.value().getPayload(), user.getUuid(), user);
        }
    }

    private List<User> getAllUsers() throws SQLException {
        ResultSet resultSet = connection.prepareStatement("SELECT uuid FROM users;").executeQuery();
        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            users.add(new User(resultSet.getString(1)));
        }

        return users;
    }
}

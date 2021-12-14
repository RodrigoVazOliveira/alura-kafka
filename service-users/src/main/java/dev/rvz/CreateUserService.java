package dev.rvz;

import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.*;
import java.util.HashMap;

public class CreateUserService {

    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_data.db";
        this.connection = DriverManager.getConnection(url);
        connection.createStatement().execute("CREATE TABLE users (" +
                "uuid VARCHAR(200) PRIMARY KEY," +
                "email VARCHAR(200) " +
                ")");
    }

    public static void main(String[] args) throws SQLException {
        CreateUserService createUserService = new CreateUserService();
        KafkaService<Order> kafkaService = new KafkaService<>(
                CreateUserService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", createUserService::parse,
                Order.class, new HashMap<>());
        kafkaService.run();
    }

    private void parse(ConsumerRecord<String, Order> orderConsumerRecord) throws SQLException {
        System.out.printf("valor: %s\n", orderConsumerRecord.value());
        if (isNewUser(orderConsumerRecord.value().getEmail())) {
            insertNewUser(orderConsumerRecord.value().getEmail());
        }
    }

    private void insertNewUser(String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (uuid, email) " +
                "VALUES (?, ?);");
        preparedStatement.setString(1, "uuid");
        preparedStatement.setString(2, email);
        preparedStatement.execute();

        System.out.println("Usuário adicionado. E-Mail: " + email);
    }

    private boolean isNewUser(String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users " +
                "WHERE email = ?;");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();

        return !resultSet.next();
    }
}

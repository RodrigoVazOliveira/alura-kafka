package dev.rvz;

import dev.rvz.consummers.ConsumerService;
import dev.rvz.consummers.ServiceRunner;
import dev.rvz.models.Message;
import dev.rvz.models.serializables.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.*;
import java.util.UUID;

public class CreateUserService implements ConsumerService<Order> {

    private final Connection connection;

    private CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_data.db";
        this.connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("CREATE TABLE users (" +
                    "uuid VARCHAR(200) PRIMARY KEY," +
                    "email VARCHAR(200) " +
                    ")");
        } catch (SQLException e) {
            // be careful,  the sql could be wrong,     be really careful
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServiceRunner<>(CreateUserService::new).start(1);
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getGroupId() {
        return CreateUserService.class.getSimpleName();
    }

    @Override
    public void parse(ConsumerRecord<String, Message<Order>> orderConsumerRecord) throws SQLException {
        System.out.printf("valor: %s\n", orderConsumerRecord.value());
        Order order = orderConsumerRecord.value().getPayload();
        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getEmail());
        }
    }

    private void insertNewUser(String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (uuid, email) " +
                "VALUES (?, ?);");
        preparedStatement.setString(1, UUID.randomUUID().toString());
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

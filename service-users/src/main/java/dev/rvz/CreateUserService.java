package dev.rvz;

import dev.rvz.consummers.ConsumerService;
import dev.rvz.consummers.ServiceRunner;
import dev.rvz.models.Message;
import dev.rvz.models.serializables.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CreateUserService implements ConsumerService<Order> {

    private final LocalDatabase database;

    private CreateUserService() throws SQLException {
        this.database = new LocalDatabase("users_data");
        this.database.createifNotExists("CREATE TABLE users (" +
                    "uuid VARCHAR(200) PRIMARY KEY," +
                    "email VARCHAR(200) " +
                    ")");
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
        this.database.update("INSERT INTO users (uuid, email) " +
                "VALUES (?, ?);", UUID.randomUUID().toString(), email);
        System.out.println("Usuário adicionado. E-Mail: " + email);
    }

    private boolean isNewUser(String email) throws SQLException {
        ResultSet resultSet = this.database.query("SELECT * FROM users " +
                "WHERE email = ?;", email);

        return !resultSet.next();
    }
}

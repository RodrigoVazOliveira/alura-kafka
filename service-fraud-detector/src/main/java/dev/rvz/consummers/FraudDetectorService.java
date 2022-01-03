package dev.rvz.consummers;

import dev.rvz.LocalDatabase;
import dev.rvz.models.Message;
import dev.rvz.models.serializables.Order;
import dev.rvz.services.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService implements ConsumerService<Order> {

    private final KafkaDispatcher<Order> kafkaDispatcher = new KafkaDispatcher<>();
    private final LocalDatabase database;

    public FraudDetectorService() throws SQLException {
        this.database = new LocalDatabase("frauds_database");
        this.database.createifNotExists("CREATE TABLE orders (" +
                "uuid VARCHAR(255) PRIMARY KEY," +
                "is_fraud BOOLEAN" +
                ");");
    }

    public static void main(String[] args) {
        new ServiceRunner<>(FraudDetectorService::new).start(1);
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getGroupId() {
        return FraudDetectorService.class.getSimpleName();
    }

    @Override
    public void parse(ConsumerRecord<String, Message<Order>> consumerRecord) throws ExecutionException, InterruptedException, SQLException {
        System.out.printf("topic: %s\n", consumerRecord.topic());
        System.out.printf("chave: %s, valor: %s, partição: %s, offset: %s\n", consumerRecord.key(), consumerRecord.value(),
                consumerRecord.partition(), consumerRecord.offset());

        Order order = consumerRecord.value().getPayload();

        if (wasProcessed(order)) {
            System.out.println("order " + order.getOrderId() + " was already processed!");
            return;
        }

        if (isaFraud(order)) {
            System.out.println("REPROVADO");
            this.database.update("INSERT INTO orders (uuid, is_fraud) VALUES (?,true)", order.getOrderId());
            kafkaDispatcher.sendMessage("ECOMMERCE_ORDER_REJECTED",
                    order.getEmail(),
                    consumerRecord.value().getId().continueWith(FraudDetectorService.class.getSimpleName()),order);
        } else {
            System.out.println("APROVADO");
            this.database.update("INSERT INTO orders (uuid, is_fraud) VALUES (?, false)", order.getOrderId());
            kafkaDispatcher.sendMessage("ECOMMERCE_ORDER_APPROVED", order.getEmail(),
                    consumerRecord.value().getId().continueWith(FraudDetectorService.class.getSimpleName()),order);
        }
    }

    private boolean wasProcessed(Order order) throws SQLException {
        ResultSet resultSet = this.database.query("SELECT uuid FROM orders WHERE uuid = ? LIMIT 1",
                order.getOrderId());

        return resultSet.next();
    }

    private boolean isaFraud(Order order) {
        return order.getAmmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}

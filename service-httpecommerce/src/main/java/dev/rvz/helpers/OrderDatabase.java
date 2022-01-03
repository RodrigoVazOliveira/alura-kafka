package dev.rvz.helpers;

import dev.rvz.LocalDatabase;
import dev.rvz.models.serializables.Order;

import java.io.Closeable;
import java.sql.SQLException;

public class OrderDatabase implements Closeable {

    private final LocalDatabase database;

    public OrderDatabase() throws SQLException {
        this.database = new LocalDatabase("orders_database");
        this.database.createifNotExists("CREATE TABLE orders (" +
                "uuid VARCHAR(255) PRIMARY KEY" +
                ");");
    }

    public boolean saveNew(Order order) throws SQLException {
        if (wasProcessed(order)) {
            return false;
        }

        this.database.update("INSERT INTO orders (uuid) VALUES (?);", order.getOrderId());
        return true;
    }

    public boolean wasProcessed(Order order) throws SQLException {
         return this.database.query("SELECT uuid FROM orders WHERE uuid = ? limit 1;", order.getOrderId())
                 .next();
    }

    @Override
    public void close() {
        try {
            this.database.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

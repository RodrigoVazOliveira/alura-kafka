package dev.rvz;

import java.sql.*;

public class LocalDatabase {

    private final Connection connection;

    public LocalDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:target/" + name + ".db";
        this.connection = DriverManager.getConnection(url);
    }

    public void createifNotExists(String sql) {
        try {
            this.connection.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String statement, String ... params) throws SQLException {
        getPreparedStatement(statement, params).execute();
    }

    public ResultSet query(String statement, String ... params) throws SQLException {
        return getPreparedStatement(statement, params).executeQuery();
    }

    private PreparedStatement getPreparedStatement(String statement, String[] params) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(statement);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setString(i+1, params[i]);
        }

        return preparedStatement;
    }

    public void close() throws SQLException {
        this.connection.close();
    }
}

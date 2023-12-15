package com.Snake.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {

    public void connect( String name, int score){
        String jdbcUrl = "jdbc:mysql://localhost:3306/PlayerRank";
        String user = "Duy";
        String password = "Anhem123";
        //launch(args);
        System.out.println("Connecting database ...");
        try {
            //launch(args);
            // Register the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
            System.out.println("Database connected!");
            // Perform database operations here

            insertData(connection, name, score);

            // Verify the data is inserted
            //verifyInsertedData(connection);

            // Close the connection
            connection.close();
            //launch(args);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Cannot connect to database");
        }
    }

    private void insertData(Connection connection, String playerName, int score) throws SQLException {
        int id = getMaxId(connection);
        String insertQuery = "INSERT INTO Players (id, pname, score) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, playerName);
            preparedStatement.setInt(3, score);
            preparedStatement.executeUpdate();
        }
    }
    private int getMaxId(Connection connection) throws SQLException {
        int maxId = 0;
        String maxIdQuery = "SELECT MAX(id) FROM Players";

        try (PreparedStatement preparedStatement = connection.prepareStatement(maxIdQuery);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }
        }

        return maxId + 1;
}
}

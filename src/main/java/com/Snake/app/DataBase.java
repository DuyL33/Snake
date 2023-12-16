package com.Snake.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {
    private String jdbcUrl = "jdbc:mysql://localhost:3306/PlayerRank";
    private String user = "Duy";
    private String password = "Anhem123";

    public void connect( String name, int score){
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
    public String getTop5Players() {

        String topPlayers = "Top 5 Single Players\n";
        
        // Sort players based on score in descending order and fetch the top 5
        String query = "SELECT pname, score FROM Players ORDER BY score DESC LIMIT 5";
        int i = 1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Open a connection
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String playerName = resultSet.getString("pname");
                int score = resultSet.getInt("score");
                Player player = new Player(playerName, score);
                topPlayers += i + ". " + player.toString();
                i++;
            }

        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return topPlayers;
        
    }
}

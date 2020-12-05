package com.zvv.database;

import core.auth.User;
import lombok.extern.log4j.Log4j2;

import java.sql.*;

@Log4j2
public class DatabaseCore {

    private static final String CONNECTION_URL = "jdbc:sqlite:server_folder/_db/zvv_cloud_server.db";
    private static Connection connection;
    private static Statement statement;

    public DatabaseCore() {
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        User user1 = new User("xx", "xx");
        User user2 = new User("test", "test");
        System.out.println(authenticateUser(user1));
        System.out.println(authenticateUser(user2));
    }

    public static boolean authenticateUser(User user) {
        String query = "SELECT count(*) as 'count' FROM users WHERE login=? and pwd=?";
        PreparedStatement preparedStatement = null;

        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPwd());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.getInt(1) <= 0) {
                return false;
            }
            return true;
        } catch (ClassNotFoundException | SQLException var8) {
            log.error(var8.getMessage(), var8);
        } finally {
            disconnect();
        }

        return false;
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:server_folder/_db/zvv_cloud_server.db");
        log.info("Connected to database {}", "jdbc:sqlite:server_folder/_db/zvv_cloud_server.db");
        statement = connection.createStatement();
    }

    private static void disconnect() {
        try {
            connection.close();
            log.info("Databaseonnection closed.");
        } catch (SQLException var1) {
            var1.printStackTrace();
        }

    }
}

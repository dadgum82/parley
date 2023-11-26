package com.sidequest.parley.dao;

import com.sidequest.parley.util.Config;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private final String DB_URL;
    private Connection connection;

    public SQLiteConnection(String dbEnv) {
        if (dbEnv.equalsIgnoreCase("prod") || dbEnv.equalsIgnoreCase("production")) {
            this.DB_URL = Config.getProperty("db.url");
        } else if (dbEnv.equalsIgnoreCase("test")) {
            this.DB_URL = Config.getProperty("db.urlTest");
        } else {
            throw new IllegalArgumentException("Unexpected dbEnv: " + dbEnv);
        }
        System.out.println(this.getDB_URL());
        connect();
    }

    public SQLiteConnection() {
        this.DB_URL = Config.getProperty("db.url");
        connect();
    }

    private void connect() {

        // TODO: this is where I switched from JNDI to DriverManager to try and get this to work with spring-boot.
//        try {
//            InitialContext context = new InitialContext();
//            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/parleydb");
//            connection = dataSource.getConnection();
//        } catch (NamingException | SQLException e) {
//            // Handle InitialContext failure
//            e.printStackTrace();
//            System.out.println("InitialContext connection failed. Falling back to DriverManager.");

        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection(DB_URL, config.toProperties());
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            // Handle DriverManager connection failure
        }
//        }
    }


    public Connection getConnection() {
        try {
            if (connection.isClosed()) {
                System.out.println("Connection was closed. Reconnecting...");
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection failure
        }
        return connection;
    }

    public String getDB_URL() {
        return this.DB_URL;
    }

    public void closeConnection() {
        close();
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// --Commented out by Inspection START (11/26/2023 8:06 AM):
//    public void printConnectionInfo() {
//        try {
//            DatabaseMetaData metaData = connection.getMetaData();
//            System.out.println("Database URL: " + metaData.getURL());
//            System.out.println("Database Username: " + metaData.getUserName());
//            System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
//            System.out.println("Database Product Version: " + metaData.getDatabaseProductVersion());
//            System.out.println("Driver Name: " + metaData.getDriverName());
//            System.out.println("Driver Version: " + metaData.getDriverVersion());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
}

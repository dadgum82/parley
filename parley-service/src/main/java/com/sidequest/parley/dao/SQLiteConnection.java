package com.sidequest.parley.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnection {

    private String dbEnv;
    private Connection connection;

    public SQLiteConnection() {
        connect();
    }

    public SQLiteConnection(String dbEnv) {
        this.dbEnv = dbEnv;
        connect();
    }

    private void connect() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/parleyDB");
            System.out.println("-!-!-!-!-!- ds: " + ds.toString());
            connection = ds.getConnection();

            // I hard coded this to enforce foreign keys because I couldn't get it to work with the SQLiteConfig
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("InitialContext connection failed. Falling back to DriverManager.");
            System.out.println(e.getMessage());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
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

    public void closeConnection() {
        close();
    }

    private void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//  //  private final String DB_URL;
//    private Connection connection;
//
//
//
//    public SQLiteConnection(String dbEnv) {
//        //Switching to application.properties for db.url
//        //TODO: 12/11/2023 this is where I switched from JNDI to DriverManager to try and get this to work with spring-boot.
////
////        if (dbEnv.equalsIgnoreCase("prod") || dbEnv.equalsIgnoreCase("production")) {
////            this.DB_URL = Config.getProperty("db.url");
////        } else if (dbEnv.equalsIgnoreCase("test")) {
////            this.DB_URL = Config.getProperty("db.urlTest");
////        } else {
////            throw new IllegalArgumentException("Unexpected dbEnv: " + dbEnv);
////        }
////        System.out.println(this.getDB_URL());
//        connect();
//    }
//
//    public SQLiteConnection() {
//     //   this.DB_URL = Config.getProperty("db.url");
//        connect();
//    }
//
//    private void connect() {
//        try {
//            DataSource dataSource = dataSource();
//            connection = dataSource.getConnection();
//
//            // I hard coded this to enforce foreign keys because I couldn't get it to work with the SQLiteConfig
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("PRAGMA foreign_keys = ON");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("InitialContext connection failed. Falling back to DriverManager.");
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//    @Bean
//    public DataSource dataSource() {
//        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
//        jndiObjectFactoryBean.setJndiName("jdbc/parleyDB");
//        try {
//            jndiObjectFactoryBean.afterPropertiesSet();
//        } catch (NamingException e) {
//            System.out.println("Error while retrieving datasource with JNDI name jdbc/parleyDB " + e);
//        }
//        return (DataSource) jndiObjectFactoryBean.getObject();
//    }
//
//
//
//
//// 12/11/2023 switching to application.properties for db.url
////    private void connect() {
////
////        // TODO: this is where I switched from JNDI to DriverManager to try and get this to work with spring-boot.
//////        try {
//////            InitialContext context = new InitialContext();
//////            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/parleydb");
//////            connection = dataSource.getConnection();
//////        } catch (NamingException | SQLException e) {
//////            // Handle InitialContext failure
//////            e.printStackTrace();
//////            System.out.println("InitialContext connection failed. Falling back to DriverManager.");
////
////        try {
////            Class.forName("org.sqlite.JDBC");
////            SQLiteConfig config = new SQLiteConfig();
////            config.enforceForeignKeys(true);
////            connection = DriverManager.getConnection(DB_URL, config.toProperties());
////        } catch (ClassNotFoundException | SQLException ex) {
////            ex.printStackTrace();
////            // Handle DriverManager connection failure
////        }
//////        }
////    }
//
//
//    public Connection getConnection() {
//        try {
//            if (connection.isClosed()) {
//                System.out.println("Connection was closed. Reconnecting...");
//                connect();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Handle connection failure
//        }
//        return connection;
//    }
//
//    public String getDB_URL() {
//      //  return this.DB_URL;
//        return Config.getProperty("db.url");
//    }
//
//    public void closeConnection() {
//        close();
//    }
//
//    public void close() {
//        try {
//            if (connection != null) {
//                connection.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public void printConnectionInfo() {
////        try {
////            DatabaseMetaData metaData = connection.getMetaData();
////            System.out.println("Database URL: " + metaData.getURL());
////            System.out.println("Database Username: " + metaData.getUserName());
////            System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
////            System.out.println("Database Product Version: " + metaData.getDatabaseProductVersion());
////            System.out.println("Driver Name: " + metaData.getDriverName());
////            System.out.println("Driver Version: " + metaData.getDriverVersion());
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////    }
//// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
}

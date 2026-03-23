package com.sportsvista.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Local development defaults
    private static final String LOCAL_URL = "jdbc:mysql://localhost:3306/sportsvista?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String LOCAL_USER = "root";
    private static final String LOCAL_PASSWORD = "root";

    private static String jdbcUrl;
    private static String user;
    private static String password;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Railway provides MYSQL_URL or MYSQLDATABASE, MYSQLHOST, MYSQLPORT, MYSQLUSER, MYSQLPASSWORD
        String railwayUrl = System.getenv("MYSQL_URL");

        if (railwayUrl != null && !railwayUrl.isEmpty()) {
            // Railway MYSQL_URL format: mysql://user:password@host:port/database
            // Convert to JDBC format: jdbc:mysql://host:port/database
            jdbcUrl = railwayUrl.replace("mysql://", "jdbc:mysql://");
            if (!jdbcUrl.contains("?")) {
                jdbcUrl += "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            }
            user = System.getenv("MYSQLUSER");
            password = System.getenv("MYSQLPASSWORD");
            System.out.println("DBConnection: Using Railway MySQL database.");
        } else {
            // Check for individual env vars
            String host = System.getenv("MYSQLHOST");
            if (host != null && !host.isEmpty()) {
                String port = System.getenv("MYSQLPORT") != null ? System.getenv("MYSQLPORT") : "3306";
                String db = System.getenv("MYSQLDATABASE") != null ? System.getenv("MYSQLDATABASE") : "railway";
                jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                user = System.getenv("MYSQLUSER");
                password = System.getenv("MYSQLPASSWORD");
                System.out.println("DBConnection: Using MySQL via env vars (host=" + host + ").");
            } else {
                // Local development fallback
                jdbcUrl = LOCAL_URL;
                user = LOCAL_USER;
                password = LOCAL_PASSWORD;
                System.out.println("DBConnection: Using local MySQL (localhost).");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
}

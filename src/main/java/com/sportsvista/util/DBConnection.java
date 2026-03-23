package com.sportsvista.util;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Local development defaults (MySQL)
    private static final String LOCAL_URL = "jdbc:mysql://localhost:3306/sportsvista?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String LOCAL_USER = "root";
    private static final String LOCAL_PASSWORD = "root";

    private static String jdbcUrl;
    private static String user;
    private static String password;
    private static boolean isPostgres = false;

    static {
        try {
            String databaseUrl = System.getenv("DATABASE_URL");
            if (databaseUrl == null || databaseUrl.isEmpty()) {
                databaseUrl = System.getenv("MYSQL_URL");
            }

            if (databaseUrl != null && !databaseUrl.isEmpty()) {
                URI uri = new URI(databaseUrl);
                String scheme = uri.getScheme();
                
                if ("postgres".equals(scheme) || "postgresql".equals(scheme)) {
                    isPostgres = true;
                    Class.forName("org.postgresql.Driver");
                    
                    String userInfo = uri.getUserInfo();
                    if (userInfo != null && userInfo.contains(":")) {
                        user = userInfo.split(":")[0];
                        password = userInfo.split(":")[1];
                    }
                    
                    int port = uri.getPort();
                    if (port == -1) port = 5432;
                    
                    jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath();
                    if (!jdbcUrl.contains("?")) {
                        jdbcUrl += "?sslmode=require";
                    }
                    System.out.println("DBConnection: Using Render PostgreSQL.");
                } else if ("mysql".equals(scheme)) {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String userInfo = uri.getUserInfo();
                    if (userInfo != null && userInfo.contains(":")) {
                        user = userInfo.split(":")[0];
                        password = userInfo.split(":")[1];
                    } else {
                        user = System.getenv("MYSQLUSER");
                        password = System.getenv("MYSQLPASSWORD");
                    }
                    
                    int port = uri.getPort();
                    if (port == -1) port = 3306;
                    
                    jdbcUrl = "jdbc:mysql://" + uri.getHost() + ":" + port + uri.getPath();
                    if (!jdbcUrl.contains("?")) {
                        jdbcUrl += "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                    }
                    System.out.println("DBConnection: Using Railway MySQL.");
                }
            } else {
                // Check for individual env vars or local dev
                String pgHost = System.getenv("PGHOST");
                if (pgHost != null) {
                    isPostgres = true;
                    Class.forName("org.postgresql.Driver");
                    String port = System.getenv("PGPORT") != null ? System.getenv("PGPORT") : "5432";
                    String db = System.getenv("PGDATABASE") != null ? System.getenv("PGDATABASE") : "sportsvista";
                    jdbcUrl = "jdbc:postgresql://" + pgHost + ":" + port + "/" + db + "?sslmode=require";
                    user = System.getenv("PGUSER");
                    password = System.getenv("PGPASSWORD");
                } else {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    jdbcUrl = LOCAL_URL;
                    user = LOCAL_USER;
                    password = LOCAL_PASSWORD;
                    System.out.println("DBConnection: Using local MySQL (localhost).");
                }
            }
        } catch (Exception e) {
            System.err.println("DBConnection Static Init Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (user != null) {
            return DriverManager.getConnection(jdbcUrl, user, password);
        }
        return DriverManager.getConnection(jdbcUrl);
    }

    public static boolean isPostgres() {
        return isPostgres;
    }
}


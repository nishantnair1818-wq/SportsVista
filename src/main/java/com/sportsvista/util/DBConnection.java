package com.sportsvista.util;

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
        // Render provides DATABASE_URL in format: postgres://user:pass@host:port/dbname
        String databaseUrl = System.getenv("DATABASE_URL");

        // Railway provides MYSQL_URL
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            databaseUrl = System.getenv("MYSQL_URL");
        }

        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            if (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://")) {
                // PostgreSQL (Render)
                isPostgres = true;
                jdbcUrl = databaseUrl.replace("postgres://", "jdbc:postgresql://")
                                     .replace("postgresql://", "jdbc:postgresql://");
                if (!jdbcUrl.contains("?")) {
                    jdbcUrl += "?sslmode=require";
                }
                user = null;
                password = null;
                try { Class.forName("org.postgresql.Driver"); } catch (Exception e) { e.printStackTrace(); }
                System.out.println("DBConnection: Using Render PostgreSQL.");
            } else if (databaseUrl.startsWith("mysql://")) {
                // MySQL (Railway)
                jdbcUrl = databaseUrl.replace("mysql://", "jdbc:mysql://");
                if (!jdbcUrl.contains("?")) {
                    jdbcUrl += "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                }
                user = System.getenv("MYSQLUSER");
                password = System.getenv("MYSQLPASSWORD");
                try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (Exception e) { e.printStackTrace(); }
                System.out.println("DBConnection: Using Railway MySQL.");
            }
        } else {
            // Check for individual Render env vars
            String pgHost = System.getenv("PGHOST");
            String mysqlHost = System.getenv("MYSQLHOST");

            if (pgHost != null && !pgHost.isEmpty()) {
                isPostgres = true;
                String port = System.getenv("PGPORT") != null ? System.getenv("PGPORT") : "5432";
                String db = System.getenv("PGDATABASE") != null ? System.getenv("PGDATABASE") : "sportsvista";
                jdbcUrl = "jdbc:postgresql://" + pgHost + ":" + port + "/" + db + "?sslmode=require";
                user = System.getenv("PGUSER");
                password = System.getenv("PGPASSWORD");
                try { Class.forName("org.postgresql.Driver"); } catch (Exception e) { e.printStackTrace(); }
                System.out.println("DBConnection: Using PostgreSQL via env vars.");
            } else if (mysqlHost != null && !mysqlHost.isEmpty()) {
                String port = System.getenv("MYSQLPORT") != null ? System.getenv("MYSQLPORT") : "3306";
                String db = System.getenv("MYSQLDATABASE") != null ? System.getenv("MYSQLDATABASE") : "railway";
                jdbcUrl = "jdbc:mysql://" + mysqlHost + ":" + port + "/" + db + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                user = System.getenv("MYSQLUSER");
                password = System.getenv("MYSQLPASSWORD");
                try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (Exception e) { e.printStackTrace(); }
                System.out.println("DBConnection: Using MySQL via env vars.");
            } else {
                // Local development
                jdbcUrl = LOCAL_URL;
                user = LOCAL_USER;
                password = LOCAL_PASSWORD;
                try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (Exception e) { e.printStackTrace(); }
                System.out.println("DBConnection: Using local MySQL (localhost).");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        if (user != null) {
            return DriverManager.getConnection(jdbcUrl, user, password);
        }
        // PostgreSQL with credentials embedded in URL
        return DriverManager.getConnection(jdbcUrl);
    }

    public static boolean isPostgres() {
        return isPostgres;
    }
}

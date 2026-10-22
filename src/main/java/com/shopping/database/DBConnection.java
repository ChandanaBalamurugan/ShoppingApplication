package com.shopping.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Creates MySQL connections. Settings come from db.properties or environment variables. */
public class DBConnection {
    private static final Properties props = new Properties();

    static {
        // Load db.properties from src/main/resources (copied to the classpath by Maven)
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) props.load(in);
        } catch (Exception e) {
            System.err.println("Could not read db.properties: " + e.getMessage());
        }
    }

    /** Environment variable wins over the file value. */
    private static String get(String envName, String propName) {
        String env = System.getenv(envName);
        return (env != null && !env.isEmpty()) ? env : props.getProperty(propName);
    }

    /** Opens a new connection. The caller must close it (use try-with-resources). */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                get("DB_URL", "db.url"), get("DB_USER", "db.user"), get("DB_PASSWORD", "db.password"));
    }
}

package com.sportsvista.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
                System.out.println("ConfigLoader: Loaded config.properties. Keys found: " + props.keySet());
            } else {
                System.out.println("ConfigLoader: config.properties not found, using environment variables only.");
            }
        } catch (Exception e) {
            System.err.println("ConfigLoader ERROR: " + e.getMessage());
        }
    }

    public static String get(String key) {
        // 1. Check environment variable first (convert dots to underscores for env var naming)
        //    e.g. "cricapi.key" -> "CRICAPI_KEY"
        String envKey = key.replace(".", "_").toUpperCase();
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.isEmpty()) {
            return envVal;
        }

        // 2. Fall back to config.properties
        return props.getProperty(key);
    }
}

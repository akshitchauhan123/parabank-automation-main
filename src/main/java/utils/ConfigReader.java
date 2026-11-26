package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        // Try reading from classpath first
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                return;
            }
        } catch (Exception e) {
            System.err.println("Could not load config.properties from classpath: " + e.getMessage());
        }

        // Fallback: Try reading from relative file path
        File file = new File("src/test/resources/config.properties");
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);
            } catch (Exception e) {
                System.err.println("Could not load config.properties from file system: " + e.getMessage());
            }
        }
    }

    public static String getProperty(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : defaultValue;
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value.trim());
        }
        return defaultValue;
    }
}


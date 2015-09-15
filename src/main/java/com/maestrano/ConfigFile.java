package com.maestrano;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ConfigFile {
    private static Map<String, Properties> properties = new HashMap<String, Properties>();

    // Private Constructor
    private ConfigFile() {
    }

    public static Properties getProperties() {
        return getProperties("default", null);
    }

    public static Properties getProperties(String preset) {
        return getProperties(preset, null);
    }

    /**
     * Load the specified configuration file for a given preset key.
     * @param preset The preset key
     * @param filename Properties filename available in the classpath
     * @return
     */
    public static Properties getProperties(String preset, String filename) {
        Properties prop = properties.get(preset);
        if (prop == null) {
            prop = loadFromFile(preset, filename);
        }

        return prop;
    }

    private static Properties loadFromFile(String preset, String filename) {
        if (filename == null) {
            filename = "config.properties";
        }
        
        InputStream input = ConfigFile.class.getClassLoader().getResourceAsStream(filename);
        if (input != null) {
            try {
                Properties prop = new Properties();
                prop.load(input);
                properties.put(preset, prop);
                return prop;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Properties file " + filename + " cannot be loaded");
    }
}

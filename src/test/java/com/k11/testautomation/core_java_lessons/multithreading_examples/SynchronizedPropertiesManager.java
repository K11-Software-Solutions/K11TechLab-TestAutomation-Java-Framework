package com.k11.testautomation.core_java_lessons.multithreading_examples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SynchronizedPropertiesManager {
    private final String filePath;
    private final Properties properties = new Properties();

    public SynchronizedPropertiesManager(String filePath) {
        this.filePath = filePath;
        loadProperties();
    }

    private synchronized void loadProperties() {
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String getProperty(String key) {
        return properties.getProperty(key);
    }

    public synchronized void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    private synchronized void saveProperties() {
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

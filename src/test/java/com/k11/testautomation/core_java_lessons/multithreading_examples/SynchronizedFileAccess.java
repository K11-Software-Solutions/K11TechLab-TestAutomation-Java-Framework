package com.k11.testautomation.core_java_lessons.multithreading_examples;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SynchronizedFileAccess {
    private final Path filePath;

    public SynchronizedFileAccess(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public synchronized List<String> readFile() {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }

    public synchronized void writeFile(List<String> lines) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file", e);
        }
    }
}

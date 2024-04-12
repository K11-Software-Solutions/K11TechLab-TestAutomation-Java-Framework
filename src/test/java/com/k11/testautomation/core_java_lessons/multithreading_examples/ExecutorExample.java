package com.k11.testautomation.core_java_lessons.multithreading_examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorExample {

    public static void main(String[] args) {
        // Create an ExecutorService with a fixed thread pool of 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Define three tasks that print out their names and simulate work by sleeping
        Runnable task1 = () -> {
            try {
                System.out.println("Executing Task1 in " + Thread.currentThread().getName());
                Thread.sleep(2000); // simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable task2 = () -> {
            try {
                System.out.println("Executing Task2 in " + Thread.currentThread().getName());
                Thread.sleep(1000); // simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable task3 = () -> {
            try {
                System.out.println("Executing Task3 in " + Thread.currentThread().getName());
                Thread.sleep(1500); // simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Execute the tasks
        executor.execute(task1);
        executor.execute(task2);
        executor.execute(task3);

        // Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted
        executor.shutdown();
    }
}

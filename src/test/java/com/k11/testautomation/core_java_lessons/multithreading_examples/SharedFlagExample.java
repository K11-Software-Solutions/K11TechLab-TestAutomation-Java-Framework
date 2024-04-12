package com.k11.testautomation.core_java_lessons.multithreading_examples;

public class SharedFlagExample {
    // The volatile keyword ensures that changes to this variable are immediately visible to other threads
    private volatile boolean flag = false;

    public void execute() {
        new Thread(() -> {
            while (!flag) {
                // Busy wait, keep looping until the flag becomes true
                System.out.println("Waiting for flag to become true...");
                try {
                    Thread.sleep(1000);  // Sleep to prevent overwhelming the console output
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted");
                }
            }
            System.out.println("Flag is true, thread is terminating.");
        }).start();

        new Thread(() -> {
            try {
                System.out.println("Sleeping for 3 seconds before setting flag to true...");
                Thread.sleep(3000);  // Sleep to simulate some processing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            flag = true;  // Update the flag, which is visible immediately to other threads
            System.out.println("Flag set to true.");
        }).start();
    }

    public static void main(String[] args) {
        new SharedFlagExample().execute();
    }
}

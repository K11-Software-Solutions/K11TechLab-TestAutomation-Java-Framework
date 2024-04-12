package com.k11.testautomation.core_java_lessons.multithreading_examples;

public class Account {
    private double balance;

    public Account(double initialBalance) {
        this.balance = initialBalance;
    }

    // Synchronized method to handle deposit
    public synchronized void deposit(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        balance += amount;
        System.out.println("Deposited: " + amount + ", New Balance: " + balance);
    }

    // Synchronized method to handle withdrawal
    public synchronized void withdraw(double amount) {
        if (amount < 0 || amount > balance) {
            throw new IllegalArgumentException("Invalid amount");
        }
        balance -= amount;
        System.out.println("Withdrew: " + amount + ", New Balance: " + balance);
    }

    // Getter for balance is also synchronized to ensure consistent state
    public synchronized double getBalance() {
        return balance;
    }

    public static void main(String[] args) {
        Account account = new Account(1000); // Initial balance of 1000

        // Thread for depositing money
        Thread depositThread = new Thread(() -> {
            account.deposit(500);
        }, "DepositThread");

        // Thread for withdrawing money
        Thread withdrawThread = new Thread(() -> {
            account.withdraw(200);
        }, "WithdrawThread");

        depositThread.start();
        withdrawThread.start();

        try {
            depositThread.join();
            withdrawThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted");
        }

        System.out.println("Final Balance: " + account.getBalance());
    }
}

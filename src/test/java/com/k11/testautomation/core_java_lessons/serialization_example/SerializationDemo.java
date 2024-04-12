package com.k11.testautomation.core_java_lessons.serialization_example;

import java.io.*;

public class SerializationDemo {
    public static void main(String[] args) {
        User user = new User("john_doe", "secret123");

        // Serialize the User object to a file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.dat"))) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize the User object from the file
        User deserializedUser = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.dat"))) {
            deserializedUser = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (deserializedUser != null) {
            System.out.println("Username: " + deserializedUser.getUsername());
            System.out.println("Password: " + deserializedUser.getPassword());
        }
    }
}

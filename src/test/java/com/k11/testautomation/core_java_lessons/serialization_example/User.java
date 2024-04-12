package com.k11.testautomation.core_java_lessons.serialization_example;

import java.io.*;

public class User implements Serializable {
    private String username;
    private transient String password;  // This field will not be serialized

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        // You can manually deserialize transient fields if needed
        // password = (String) ois.readObject();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        // You can manually serialize transient fields if needed
        // oos.writeObject(password);
    }
}


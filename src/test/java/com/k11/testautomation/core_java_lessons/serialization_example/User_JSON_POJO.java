package com.k11.testautomation.core_java_lessons.serialization_example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User_JSON_POJO {
    private String name;
    private int age;
    @JsonIgnore  // This annotation tells Jackson to ignore this field during serialization
    private String password;  // This field will not be serialized

    // Constructors, getters, and setters
    public User_JSON_POJO() {}

    public User_JSON_POJO(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static void main(String[] args) {
        User_JSON_POJO user = new User_JSON_POJO("John Doe", 30, "secretPassword");
        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonString = mapper.writeValueAsString(user);
            System.out.println(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}

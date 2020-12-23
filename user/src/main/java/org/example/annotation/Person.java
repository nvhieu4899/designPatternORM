package org.example.annotation;

public class Person {
    private String name;

    private int age;

    @BuilderProperty
    public void setName(String name) {
        this.name = name;
    }

    @BuilderProperty
    public void setAge(int age) {
        this.age = age;
    }
}

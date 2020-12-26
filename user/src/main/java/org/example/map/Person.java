package org.example.map;

@Table
public class Person {
    private int id;
    @Column(fieldName = "personName")
    private String name;
}

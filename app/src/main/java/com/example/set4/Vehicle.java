package com.example.set4;

public abstract class Vehicle {
    private final int id;
    private String name;
    private static int nextId;

    public Vehicle(String name) {
        this.id = nextId++;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public abstract String toString();
}

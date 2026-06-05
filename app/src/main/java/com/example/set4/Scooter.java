package com.example.set4;

public class Scooter extends Vehicle {
    public Scooter(String name) {
        super(name);
    }

    public String toString() {
        return "[" + getId() + "] Scooter " + getName();
    }
}

package com.example.set4;

public class Garage {
    private final int number;
    private Parkable parkedVehicle;

    public Garage(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isEmpty() {
        return parkedVehicle == null;
    }

    public Parkable getParkedVehicle() {
        return parkedVehicle;
    }

    void setParkedVehicle(Parkable parkedVehicle) {
        this.parkedVehicle = parkedVehicle;
    }
}

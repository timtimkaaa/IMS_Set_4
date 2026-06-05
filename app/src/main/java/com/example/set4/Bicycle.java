package com.example.set4;

public class Bicycle extends Vehicle implements Parkable {
    private Garage garage;

    public Bicycle(String name) {
        super(name);
    }

    public boolean park(Garage garage) {
        if (garage == null || !garage.isEmpty() || isParked()) {
            return false;
        }
        this.garage = garage;
        garage.setParkedVehicle(this);
        return true;
    }

    public boolean unpark() {
        if (!isParked()) {
            return false;
        }
        Garage oldGarage = garage;
        garage = null;
        oldGarage.setParkedVehicle(null);
        return true;
    }

    public boolean isParked() {
        return garage != null;
    }

    public Garage getGarage() {
        return garage;
    }

    public String toString() {
        String parkedText = isParked() ? "yes, garage " + garage.getNumber() : "no, garage -";
        return "[" + getId() + "] Bicycle " + getName()
                + " | parked: " + parkedText;
    }
}

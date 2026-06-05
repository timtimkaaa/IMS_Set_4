package com.example.set4;

public class Car extends Vehicle implements CombustionVehicle, Parkable {
    private final int supportedFuelMask;
    private double fuelAmount;
    private Garage garage;

    public Car(String name, int supportedFuelMask) {
        this(name, supportedFuelMask, 0.0);
    }

    public Car(String name, int supportedFuelMask, double fuelAmount) {
        super(name);
        this.supportedFuelMask = supportedFuelMask;
        this.fuelAmount = Math.max(0.0, fuelAmount);
    }

    public boolean refuel(int fuelMask, double liters) {
        if (liters <= 0) {
            return false;
        }
        if ((supportedFuelMask & fuelMask) == 0) {
            return false;
        }
        fuelAmount += liters;
        return true;
    }

    public int getSupportedFuelMask() {
        return supportedFuelMask;
    }

    public double getFuelAmount() {
        return fuelAmount;
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
        return "[" + getId() + "] Car " + getName()
                + " | fuels: " + supportedFuelMask
                + " | fuel amount: " + fuelAmount + " l"
                + " | parked: " + parkedText;
    }
}

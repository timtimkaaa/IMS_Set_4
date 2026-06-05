package com.example.set4;

public class Motorboat extends Vehicle implements CombustionVehicle {
    private final int supportedFuelMask;
    private double fuelAmount;

    public Motorboat(String name, int supportedFuelMask) {
        this(name, supportedFuelMask, 0.0);
    }

    public Motorboat(String name, int supportedFuelMask, double fuelAmount) {
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

    public String toString() {
        return "[" + getId() + "] Motorboat " + getName()
                + " | fuels: " + supportedFuelMask
                + " | fuel amount: " + fuelAmount + " l";
    }
}

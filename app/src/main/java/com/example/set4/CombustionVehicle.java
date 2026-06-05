package com.example.set4;

public interface CombustionVehicle {
    // XML fuelType mapping:
    // 1 = DIESEL, 2 = PETROL, 4 = LPG, 8 = CNG.
    // Combinations are saved as sums, for example 3 means DIESEL + PETROL.
    public static final int DIESEL = 1 << 0;
    public static final int PETROL = 1 << 1;
    public static final int LPG = 1 << 2;
    public static final int CNG = 1 << 3;

    public boolean refuel(int fuelMask, double liters);

    public int getSupportedFuelMask();

    public double getFuelAmount();
}

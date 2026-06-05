package com.example.set4;

public interface Parkable {
    public boolean park(Garage garage);

    public boolean unpark();

    public boolean isParked();

    public Garage getGarage();
}

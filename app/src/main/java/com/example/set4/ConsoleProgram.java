package com.example.set4;

import java.util.Scanner;

public class ConsoleProgram {
    public static void main(String[] args) {
        String fileName = "vehicles.xml";
        Rental rental = new Rental(5);
        rental.loadFromXml(fileName);
        rental.runMenu(new Scanner(System.in), fileName);
    }
}

package com.example.set4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Rental {
    private final ArrayList<Vehicle> vehicles;
    private final ArrayList<Garage> garages;

    public Rental(int garageCount) {
        vehicles = new ArrayList<>();
        garages = new ArrayList<>();
        for (int i = 1; i <= garageCount; i++) {
            garages.add(new Garage(i));
        }
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public ArrayList<Garage> getGarages() {
        return garages;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public Vehicle findVehicleById(int id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == id) {
                return vehicle;
            }
        }
        return null;
    }

    public Garage findGarageByNumber(int number) {
        for (Garage garage : garages) {
            if (garage.getNumber() == number) {
                return garage;
            }
        }
        return null;
    }

    public String parkVehicleInGarage(int vehicleId, int garageNumber) {
        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle == null) {
            return "Failure: vehicle not found.";
        }
        if (!(vehicle instanceof Parkable)) {
            return "Failure: vehicle not parkable.";
        }

        Garage garage = findGarageByNumber(garageNumber);
        if (garage == null) {
            return "Failure: garage not found.";
        }
        if (!garage.isEmpty()) {
            return "Failure: garage occupied.";
        }

        Parkable parkable = (Parkable) vehicle;
        if (parkable.isParked()) {
            return "Failure: vehicle already parked.";
        }
        if (parkable.park(garage)) {
            return "Success: vehicle parked in garage " + garageNumber + ".";
        }
        return "Failure: could not park vehicle.";
    }

    public String removeVehicleById(int id) {
        Vehicle vehicle = findVehicleById(id);
        if (vehicle == null) {
            return "Failure: vehicle not found.";
        }
        if (vehicle instanceof Parkable) {
            Parkable parkable = (Parkable) vehicle;
            if (parkable.isParked()) {
                parkable.unpark();
            }
        }
        vehicles.remove(vehicle);
        return "Success: vehicle removed. Parked vehicle was automatically unparked if needed.";
    }

    public void sortVehicles() {
        Collections.sort(vehicles, new Comparator<Vehicle>() {
            public int compare(Vehicle first, Vehicle second) {
                int result = Boolean.compare(isParked(second), isParked(first));
                if (result != 0) {
                    return result;
                }

                result = Integer.compare(typeOrder(first), typeOrder(second));
                if (result != 0) {
                    return result;
                }

                result = first.getName().compareToIgnoreCase(second.getName());
                if (result != 0) {
                    return result;
                }

                result = Integer.compare(fuelMask(first), fuelMask(second));
                if (result != 0) {
                    return result;
                }

                return Double.compare(fuelAmount(first), fuelAmount(second));
            }
        });
    }

    public String printAllVehicles() {
        sortVehicles();
        StringBuilder builder = new StringBuilder();
        for (Vehicle vehicle : vehicles) {
            builder.append(vehicle.toString()).append('\n');
        }
        return builder.toString();
    }

    public void addNewVehicleFromKeyboard(Scanner scanner) {
        System.out.println("Choose type: 1-car, 2-motorboat, 3-bicycle, 4-scooter");
        int type = readInt(scanner);
        System.out.println("Enter name:");
        String name = scanner.nextLine();

        if (type == 1) {
            System.out.println("Enter fuelType mask (1 diesel, 2 petrol, 4 LPG, 8 CNG, use sums):");
            int fuelType = readInt(scanner);
            System.out.println("Enter initial fuel amount in liters:");
            double fuel = readDouble(scanner);
            addVehicle(new Car(name, fuelType, fuel));
            System.out.println("Car added.");
        } else if (type == 2) {
            System.out.println("Enter fuelType mask (1 diesel, 2 petrol, 4 LPG, 8 CNG, use sums):");
            int fuelType = readInt(scanner);
            System.out.println("Enter initial fuel amount in liters:");
            double fuel = readDouble(scanner);
            addVehicle(new Motorboat(name, fuelType, fuel));
            System.out.println("Motorboat added.");
        } else if (type == 3) {
            addVehicle(new Bicycle(name));
            System.out.println("Bicycle added.");
        } else if (type == 4) {
            addVehicle(new Scooter(name));
            System.out.println("Scooter added.");
        } else {
            System.out.println("Wrong vehicle type.");
        }
    }

    public void runMenu(Scanner scanner, String xmlFileName) {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1. Park a vehicle in a garage");
            System.out.println("2. Add a new vehicle");
            System.out.println("3. Remove a vehicle by ID");
            System.out.println("4. Print all vehicles");
            System.out.println("5. Save and exit");
            int choice = readInt(scanner);

            if (choice == 1) {
                System.out.println("Vehicle ID:");
                int vehicleId = readInt(scanner);
                System.out.println("Garage number:");
                int garageNumber = readInt(scanner);
                System.out.println(parkVehicleInGarage(vehicleId, garageNumber));
            } else if (choice == 2) {
                addNewVehicleFromKeyboard(scanner);
            } else if (choice == 3) {
                System.out.println("Vehicle ID:");
                int vehicleId = readInt(scanner);
                System.out.println(removeVehicleById(vehicleId));
            } else if (choice == 4) {
                System.out.print(printAllVehicles());
            } else if (choice == 5) {
                saveToXml(xmlFileName);
                running = false;
            } else {
                System.out.println("Wrong menu option.");
            }
        }
    }

    public void loadFromXml(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodes = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element element = (Element) node;
                String tag = element.getTagName();
                String name = getText(element, "name");

                if ("car".equals(tag)) {
                    addVehicle(new Car(name, readFuelType(element)));
                } else if ("motorboat".equals(tag)) {
                    addVehicle(new Motorboat(name, readFuelType(element)));
                } else if ("bicycle".equals(tag)) {
                    addVehicle(new Bicycle(name));
                } else if ("scooter".equals(tag)) {
                    addVehicle(new Scooter(name));
                }
            }
        } catch (Exception exception) {
            System.out.println("XML loading error: " + exception.getMessage());
        }
    }

    public void saveToXml(String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("vehicles");
            document.appendChild(root);

            for (Vehicle vehicle : vehicles) {
                Element vehicleElement = document.createElement(xmlTagFor(vehicle));
                Element nameElement = document.createElement("name");
                nameElement.appendChild(document.createTextNode(vehicle.getName()));
                vehicleElement.appendChild(nameElement);

                if (vehicle instanceof CombustionVehicle) {
                    Element fuelElement = document.createElement("fuelType");
                    fuelElement.appendChild(document.createTextNode(String.valueOf(fuelMask(vehicle))));
                    vehicleElement.appendChild(fuelElement);
                }

                root.appendChild(vehicleElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
        } catch (Exception exception) {
            System.out.println("XML saving error: " + exception.getMessage());
        }
    }

    private int readFuelType(Element element) {
        String text = getText(element, "fuelType");
        if (text.length() == 0) {
            return 0;
        }
        return Integer.parseInt(text);
    }

    private String getText(Element element, String tagName) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list.getLength() == 0) {
            return "";
        }
        return list.item(0).getTextContent();
    }

    private String xmlTagFor(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            return "car";
        }
        if (vehicle instanceof Motorboat) {
            return "motorboat";
        }
        if (vehicle instanceof Bicycle) {
            return "bicycle";
        }
        return "scooter";
    }

    private boolean isParked(Vehicle vehicle) {
        return vehicle instanceof Parkable && ((Parkable) vehicle).isParked();
    }

    private int typeOrder(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            return 1;
        }
        if (vehicle instanceof Motorboat) {
            return 2;
        }
        if (vehicle instanceof Bicycle) {
            return 3;
        }
        return 4;
    }

    private int fuelMask(Vehicle vehicle) {
        if (vehicle instanceof CombustionVehicle) {
            return ((CombustionVehicle) vehicle).getSupportedFuelMask();
        }
        return 0;
    }

    private double fuelAmount(Vehicle vehicle) {
        if (vehicle instanceof CombustionVehicle) {
            return ((CombustionVehicle) vehicle).getFuelAmount();
        }
        return 0.0;
    }

    private int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Enter a number:");
            scanner.nextLine();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private double readDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("Enter a number:");
            scanner.nextLine();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
}

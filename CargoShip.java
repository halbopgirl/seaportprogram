//File: CargoShip.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Defines a CargoShip object as a type of Ship and contains related methods


package seaportprogram;

import java.util.Scanner;

public class CargoShip extends Ship{
    //variables
    private double cargoValue;
    private double cargoVolume;
    private double cargoWeight;
    
    //constructors, toString methods, and other appropriate methods
    //general constructor
    public CargoShip (String name, int index, int parent, double weight, 
            double length, double width, double draft, double cargWeight, 
            double volume, double value) {
        super(name, index, parent, weight, length, width, draft);
        cargoValue = value;
        cargoVolume = volume;
        cargoWeight = cargWeight;
    }
    
    //scanner constructor
   public CargoShip(Scanner scanIn) {
       super(scanIn);
       if(scanIn.hasNextDouble()) cargoWeight = scanIn.nextDouble();
       if(scanIn.hasNextDouble()) cargoVolume = scanIn.nextDouble();
       if(scanIn.hasNextDouble()) cargoValue = scanIn.nextDouble();
   }
    
   //overrides parent toString method
    @Override
    public String toString() {
        String cargoShipString = "Cargo " + super.toString();
        return cargoShipString;
    }
}

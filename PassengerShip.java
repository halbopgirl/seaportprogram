//File: PassengerShip.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Defines a Passenger Ship object and includes related methods


package seaportprogram;

import java.util.Scanner;

public class PassengerShip extends Ship{
    //variables
    private int numberOfOccupiedRooms;
    private int numberOfPassengers;
    private int numberOfRooms;
    
    //constructors, toString methods, and other appropriate methods
    //general constructor
    public PassengerShip (String name, int index, int parent, double weight, 
            double length, double width, double draft, int numPassengers, 
            int numRooms, int numOccupied) {
        super(name, index, parent, weight, length, width, draft);
        numberOfPassengers = numPassengers;
        numberOfRooms = numRooms;
        numberOfOccupiedRooms = numOccupied;
    }
    
    //scanner constructor
    public PassengerShip(Scanner scanIn) {
        super(scanIn);
        if(scanIn.hasNextInt()) numberOfPassengers = scanIn.nextInt();
        if(scanIn.hasNextInt()) numberOfRooms = scanIn.nextInt();
        if(scanIn.hasNextInt()) numberOfOccupiedRooms = scanIn.nextInt();
    }
    
    //overrides parent toString method
    @Override
    public String toString() {
        String paxShipString = "Passenger " + super.toString();
        return paxShipString;
    }
}

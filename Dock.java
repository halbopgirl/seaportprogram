//File: Dock.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Defines Dock object and contains related methods


package seaportprogram;
//

import java.util.Scanner;

public class Dock extends Thing{
    //variables
    private Ship ship;
    
    //constructors, toString methods, and other appropriate methods
    //general constructors
    public Dock(String name, int index, int parent) {
       super(name, index, parent); 
    }
    public Dock(String name, int index, int parent, Ship thisShip) {
       super(name, index, parent); 
       ship = thisShip;
    }
    
    //scanner constructor
    public Dock(Scanner scanIn) {
        super(scanIn);
    }
    
    //method to add a ship to an existing dock without one
    public void addShip(Ship myShip) {
        ship = myShip;
    }
    
    //overrides parent toString method
    @Override
    public String toString() {
        String dockString = "Dock: " + super.toString();
        return dockString;
    }
}

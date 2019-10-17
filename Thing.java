//File: Thing.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Defines a general thing object which all other objects in the program descend from 


package seaportprogram;

import java.util.Comparator;
import java.util.Scanner;

public class Thing implements Comparable<Thing>{
    //variables
    private int index;
    private String name;
    private int parent = 0;

    //overrides parent compareTo method to compare Things by name
    @Override
    public int compareTo(Thing thing) {
        String compareName = thing.getName();
        String thisName = this.getName();
        return thisName.compareToIgnoreCase(compareName);
    }
    
    //constructors, toString methods, and other appropriate methods
    //general constructor
    public Thing(String portName, int portIndex, int parentIndex) {
      name = portName;
      index = portIndex;
      parent = parentIndex;
    }
    //default empty constructor
    public Thing() {
        name = "Default";
        index = 0;
        parent = 0;
    }
    //scanner constructor
    public Thing(Scanner scanIn) {
        if(scanIn.hasNext()) name = scanIn.next();
        if(scanIn.hasNextInt()) index = scanIn.nextInt();
        if(scanIn.hasNextInt()) parent = scanIn.nextInt();
    }
    
    //getter methods
    public String getName() {
        return name;
    }
    public int getIndex() {
        return index;
    }
    public int getParent() {
        return parent;
    }
    public void setParent(int newParent) {
        parent = newParent;
    }
    
    //overrides Object's toString method
    @Override
    public String toString() {
        return name + " " + index;
    }
    
    //comparator to compare names
    private static Comparator<Thing> nameComparator = new Comparator<Thing>() {
        @Override
        public int compare (Thing thing1, Thing thing2) {
            return thing1.compareTo(thing2);
        }
    };
    
    //getter for comparator
    public Comparator<Thing> getComparator() {
        return nameComparator;
    }
    
    
}

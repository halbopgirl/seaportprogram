//File: SeaPort.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Defines a seaport object and contains related methods
package seaportprogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import static seaportprogram.Ship.draftComparator;
import static seaportprogram.Ship.lengthComparator;
import static seaportprogram.Ship.weightComparator;
import static seaportprogram.Ship.widthComparator;

public class SeaPort extends Thing {

    //define lists of elements which make up a seaport
    private ArrayList<Dock> docks = new ArrayList<Dock>();
    private ArrayList<Ship> que = new ArrayList<Ship>();
    private ArrayList<Ship> ships = new ArrayList<Ship>();
    private ArrayList<Person> persons = new ArrayList<Person>();
    int count = 0;

    //constructors, toString methods, and other appropriate methods
    //general constructor
    public SeaPort(String name, int index, int parent, ArrayList<Dock> portDocks,
            ArrayList<Ship> portQue, ArrayList<Ship> portShips, ArrayList<Person> portPeople) {
        super(name, index, parent);
        docks = portDocks;
        que = portQue;
        ships = portShips;
        persons = portPeople;
    }

    //scanner constructor
    public SeaPort(Scanner scanIn) {
        super(scanIn);
    }

    //methods to add elements to the ArrayLists
    public void addDock(Dock dock) {
        docks.add(dock);
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public void addShipToQue(Ship ship) {
        que.add(ship);
    }

    public void addPerson(Person dude) {
        persons.add(dude);
    }

    //getter methods
    public ArrayList<Dock> getDocks() {
        return docks;
    }

    public ArrayList<Ship> getQue() {
        return que;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public ArrayList<Person> getPeople() {
        return persons;
    }

    //overridden toString method
    @Override
    public String toString() {
        String seaPortString = "\n\n\nSeaPort: " + super.toString() + "\n";
        for (Dock dockName : docks) {
            seaPortString += "\n" + dockName;
        }
        seaPortString += "\n\n --- List of all ships in que:";
        for (Ship ship : que) {
            seaPortString += "\n   > " + ship;
        }
        seaPortString += "\n\n --- List of all ships:";
        for (Ship ship : ships) {
            seaPortString += "\n   > " + ship;
        }
        seaPortString += "\n\n --- List of all persons:";
        for (Person nameOfPerson : persons) {
            seaPortString += "\n   > " + nameOfPerson;
        }
        return seaPortString;
    }

    //Sort-Related Methods
    public void sortByShipName(ArrayList<Ship> thingList) {
        Collections.sort(thingList, this.getComparator());
    }

    public void sortByDockName(ArrayList<Dock> thingList) {
        Collections.sort(thingList, this.getComparator());
    }

    public void sortByPersonName(ArrayList<Person> thingList) {
        Collections.sort(thingList, this.getComparator());
    }

    public void sortShipsByWeight(ArrayList<Ship> thingList) {
        Collections.sort(thingList, this.getWeightComparator());
    }

    public void sortShipsByLength(ArrayList<Ship> thingList) {
        Collections.sort(thingList, this.getLengthComparator());
    }

    public void sortShipsByWidth(ArrayList<Ship> thingList) {
        Collections.sort(thingList, this.getWidthComparator());
    }

    public void sortShipsByDraft(ArrayList<Ship> thingList) {
        Collections.sort(thingList, this.getDraftComparator());
    }

    //Retrieve comparators from Ship class
    public Comparator<Ship> getWeightComparator() {
        return weightComparator;
    }

    public Comparator<Ship> getLengthComparator() {
        return lengthComparator;
    }

    public Comparator<Ship> getWidthComparator() {
        return widthComparator;
    }

    public Comparator<Ship> getDraftComparator() {
        return draftComparator;
    }

    public void dockNext(Dock dock) {
        synchronized (que) {
            if (!que.isEmpty()) {
                Ship ship = que.get(0);
                ship.setParent(dock.getIndex());
                ship.dockAndRun(dock);
                que.remove(ship);
            }
        }
    }

}

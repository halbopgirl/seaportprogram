//File: Ship.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Defines ship object and contains related methods
package seaportprogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

public class Ship extends Thing {

    //variables
    private PortTime arrivalTime;
    private PortTime dockTime;
    private double draft;
    private double length;
    private double width;
    public double weight;
    private ArrayList<Job> jobs = new ArrayList<>();
    private boolean isDocked = false;
    private boolean hasDocked = false;

    //constructors, toString methods, and other appropriate methods
    //general constructor
    public Ship(String name, int index, int parent, double shipWeight,
            double shipLength, double shipWidth, double shipDraft) {
        super(name, index, parent);
        weight = shipWeight;
        length = shipLength;
        width = shipWidth;
        draft = shipDraft;
    }

    //scanner constructor
    public Ship(Scanner scanIn) {
        super(scanIn);
        if (scanIn.hasNextDouble()) {
            weight = scanIn.nextDouble();
        }
        if (scanIn.hasNextDouble()) {
            length = scanIn.nextDouble();
        }
        if (scanIn.hasNextDouble()) {
            width = scanIn.nextDouble();
        }
        if (scanIn.hasNextDouble()) {
            draft = scanIn.nextDouble();
        }
    }

    //overrides parent toString method
    @Override
    public String toString() {
        String shipString = "Ship: " + super.toString();
        return shipString;
    }

    //getters
    public double getDraft() {
        return draft;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getWeight() {
        return weight;
    }

    //methods to compare various aspects of the ships
    public int compareDraft(Ship ship) {
        double compareDraft = ship.getDraft();
        double thisDraft = this.getDraft();
        if (thisDraft > compareDraft) {
            return 1;
        } else if (thisDraft == compareDraft) {
            return 0;
        } else {
            return -1;
        }
    }

    public int compareLength(Ship ship) {
        double compareLength = ship.getLength();
        double thisLength = this.getLength();
        if (thisLength > compareLength) {
            return 1;
        } else if (thisLength == compareLength) {
            return 0;
        } else {
            return -1;
        }
    }

    public int compareWidth(Ship ship) {
        double compareWidth = ship.getWidth();
        double thisWidth = this.getWidth();
        if (thisWidth > compareWidth) {
            return 1;
        } else if (thisWidth == compareWidth) {
            return 0;
        } else {
            return -1;
        }
    }

    public int compareWeight(Ship ship) {
        double compareWeight = ship.getWeight();
        double thisWeight = this.getWeight();
        if (thisWeight > compareWeight) {
            return 1;
        } else if (thisWeight == compareWeight) {
            return 0;
        } else {
            return -1;
        }
    }

    //Comparators to sort Ships by their comparisons
    public static Comparator<Ship> draftComparator = new Comparator<Ship>() {
        @Override
        public int compare(Ship ship1, Ship ship2) {
            return ship1.compareDraft(ship2);
        }
    };

    public static Comparator<Ship> lengthComparator = new Comparator<Ship>() {
        @Override
        public int compare(Ship ship1, Ship ship2) {
            return ship1.compareLength(ship2);
        }
    };

    public static Comparator<Ship> widthComparator = new Comparator<Ship>() {
        @Override
        public int compare(Ship ship1, Ship ship2) {
            return ship1.compareWidth(ship2);
        }
    };

    public static Comparator<Ship> weightComparator = new Comparator<Ship>() {
        @Override
        public int compare(Ship ship1, Ship ship2) {
            return ship1.compareWeight(ship2);
        }
    };
    
    public void addJob(Job myJob) {
        jobs.add(myJob);
    }
    
    public void setArrivalTime() {
        arrivalTime = new PortTime(System.currentTimeMillis());
    }
    
    public void setDockTime() {
        dockTime = new PortTime(System.currentTimeMillis());
    }
    
    public void dockAndRun(Dock dock) {
        dockTime = new PortTime(System.currentTimeMillis());;
        isDocked = true;
        for (Job job : jobs) {
            job.setParentDock(dock);
        }    
    }
    
    public boolean isDocked() {
        return isDocked;
    }
    
    public boolean hasDocked() {
        return hasDocked;
    }
    
    public boolean jobsComplete() {
        for (Job job : jobs) {
            if (!job.getDone()) {
                return false;
            }
        }
        isDocked = false;
        hasDocked = true;
      return true;
    }
    
}

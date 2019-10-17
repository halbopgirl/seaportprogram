//File: Person.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Defines a Person object and contains related methods

package seaportprogram;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Person extends Thing {
    //variables
    private String skill;
    private final Lock lock = new ReentrantLock();
    private boolean isWorking = false;

    //constructors, toString methods, and other appropriate methods
    //general constructor
    public Person(String name, int index, int parent, String jobSkill) {
        super(name, index, parent);
        skill = jobSkill;
    }
    
    //scanner constructor
    public Person(Scanner scanIn) {
        super(scanIn);
        if(scanIn.hasNext()) skill = scanIn.next();
    }
    
    //getter
    public String getSkill() {
        return skill;
    }

    //overrides parent toString method
    @Override
    public String toString() {
        String personInfo = "Person: " + super.toString() + " " + skill;
        return personInfo;
    }
    
    public boolean hasSkill(String skillName) {
        /*if (skill.contains(skillName)) {
            System.out.println(this.getName() + " is a " + skillName + " and can do the job!");
        }*/
        return skill.contains(skillName);
    }
    
    public void isWorking() {
        isWorking = true;
    }
    
    public void doneWorking() {
        isWorking = false;
    }
    
    public boolean getWorking() {
        return isWorking;
    }
   
}

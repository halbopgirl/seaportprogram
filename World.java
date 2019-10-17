//File: World.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Defines a world object which contains methods relating to every seaport therein
package seaportprogram;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class World extends Thing {

    //variables
    private ArrayList<SeaPort> ports;
    private PortTime time;
    private String allData;
    private JPanel jobsPanel;

    //constructors, toString methods, and other appropriate methods
    //constructor
    public World(String name, int index, int parent, File fileChosen) {
        super(name, index, parent);
        ports = new ArrayList<>();
        time = new PortTime(System.currentTimeMillis());
        allData = "";
        jobsPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 5;
        jobsPanel.setLayout(layout);
        
        //open the file
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileChosen));
            while (in.ready()) {
                String fileData = in.readLine();
                String[] lineData = fileData.split(" ");
                allData += fileData + "\n";
            }
            in.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IO Exception");
        }
    }

    //overrides parent toString method
    @Override
    public String toString() {
        process(allData);
        System.out.println(allData);
        String portString = "";
        for (SeaPort seaPort : ports) {
            portString += seaPort.toString();
        }
        return ">>>>> The world:\n" + portString;
    }
    
    public String sortedString() {
        String portString = "";
        for (SeaPort seaPort : ports) {
            portString += seaPort.toString();
        }
        return ">>>>> The world:\n" + portString;
    }

    //Handling a line from a file:
    public void process(String st) {
        Scanner scanIn = new Scanner(st.trim().toLowerCase());
        readFile(scanIn);
        scanIn.close();
    }//end process

    public void readFile(Scanner scanIn) {
        //declare HashMaps
        HashMap<Integer, Ship> shipMap = new HashMap<Integer, Ship>();
        HashMap<Integer, SeaPort> portMap = new HashMap<Integer, SeaPort>();
        HashMap<Integer, Dock> dockMap = new HashMap<Integer, Dock>();
        //HashMap<Integer, Person> personMap = new HashMap<Integer, Person>();

        while (scanIn.hasNextLine()) {
            String nextLine = scanIn.nextLine();
            Scanner scanLine = new Scanner(nextLine);
            if (scanLine.hasNext()) {
                switch (scanLine.next()) {
                    case "port":
                        SeaPort thisPort = new SeaPort(scanLine);
                        ports.add(thisPort);
                        portMap.put(thisPort.getIndex(), thisPort);
                        System.out.println("port added: " + thisPort.getIndex());
                        break;
                    case "dock":
                        Dock thisDock = new Dock(scanLine);
                        dockMap.put(thisDock.getIndex(), thisDock);
                        this.assignDock(thisDock, portMap);
                        System.out.println("dock added: " + thisDock.getIndex());
                        break;
                    case "pship":
                        PassengerShip thisPaxShip = new PassengerShip(scanLine);
                        shipMap.put(thisPaxShip.getIndex(), thisPaxShip);
                        this.assignShip(thisPaxShip, portMap, dockMap);
                        System.out.println("ship added: " + thisPaxShip.getName() + " to " + thisPaxShip.getParent());
                        break;
                    case "cship":
                        CargoShip thisCargoShip = new CargoShip(scanLine);
                        shipMap.put(thisCargoShip.getIndex(), thisCargoShip);
                        this.assignShip(thisCargoShip, portMap, dockMap);
                        System.out.println("ship added: " + thisCargoShip.getName() + " to " + thisCargoShip.getParent());
                        break;
                    case "person":
                        Person thisPerson = new Person(scanLine);
                        this.assignPerson(thisPerson, portMap);
                        //personMap.put(thisPerson.getIndex(), thisPerson);
                        break;
                    case "job":
                        Job thisJob = new Job(ports, jobsPanel, scanLine);
                        this.assignJob(thisJob, shipMap);
                        break;
                    case "//":
                        break;
                    default:
                        break;//do nothing
                }//end switch
            }//end if
        }//end while
    }

    public ArrayList<SeaPort> getPorts() {
     return ports;   
    }
    
    //Methods for finding things by index
    public Ship getShipByIndex(int x, java.util.HashMap<Integer, Ship> shipMap) {
        try {
            return shipMap.get(x);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No Such Ship");
        }
        return null;
    } // end getShipByIndex 

    public Dock getDockByIndex(int x, java.util.HashMap<Integer, Dock> dockMap) {
        try {
            return dockMap.get(x);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No Such Dock");
        }
        return null;
    } // end getDockByIndex 

    public SeaPort getSeaPortByIndex(int x, java.util.HashMap<Integer, SeaPort> portMap) {
        try {
            return portMap.get(x);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No Such Port dude");
        }
        return null;
    } // end getSeaPortByIndex 

    //I think this is unneccessary but it could be useful later
    /* public Person getPersonByIndex(int x, java.util.HashMap<Integer, Person> personMap) {
        try {
            return personMap.get(x);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No Such Person");
        }
        return null;
    } // end getPersonByIndex 
     */
    //add ship to a seaport
    public void assignShip(Ship ship, HashMap<Integer, SeaPort> portMap, HashMap<Integer, Dock> dockMap) {
        SeaPort seaPort = getSeaPortByIndex(ship.getParent(), portMap);
        Dock dock = getDockByIndex(ship.getParent(), dockMap);
        if (seaPort != null) {
            seaPort.addShip(ship);
            seaPort.addShipToQue(ship);
            ship.setArrivalTime();
        } else if (dock != null) {
            seaPort = getSeaPortByIndex(dock.getParent(), portMap);
            if (seaPort != null) {
                seaPort.addShip(ship);
                ship.setArrivalTime();
                ship.setDockTime();
            } else {
                JOptionPane.showMessageDialog(null, "No Such Port: " + ship.getParent());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Such Port: " + ship.getParent());
        }
    } // end method assignShip 

    //add dock to seaport
    public void assignDock(Dock dock, HashMap<Integer, SeaPort> portMap) {
        SeaPort seaPort = getSeaPortByIndex(dock.getParent(), portMap);
        if (seaPort != null) {
            seaPort.addDock(dock);
        } else {
            JOptionPane.showMessageDialog(null, "No Such Port: " + dock.getParent());
        }
    } // end method assignDock 

    //add person to seaport
    public void assignPerson(Person person, HashMap<Integer, SeaPort> portMap) {
        SeaPort seaPort = getSeaPortByIndex(person.getParent(), portMap);
        if (seaPort != null) {
            seaPort.addPerson(person);
        } else {
            JOptionPane.showMessageDialog(null, "No Such Port: " + person.getParent());
        }
    } // end method assignDock 
    
    //add job to ship
    public void assignJob(Job job, HashMap<Integer, Ship> shipMap) {
        Ship ship = getShipByIndex(job.getParent(), shipMap);
        if (ship != null) {
            ship.addJob(job);
        } else {
            JOptionPane.showMessageDialog(null, "No Such Ship: " + job.getParent());
        }
    }

    //method for searching everything by name
    public String searchNames(String name) {
        String nameString = "";
        for (SeaPort seaPort : ports) {
            if (seaPort.getName().equalsIgnoreCase(name.trim())) {
                nameString += (seaPort.toString() + "\n");
            }
            for (Dock dock : seaPort.getDocks()) {
                if (dock.getName().equalsIgnoreCase(name.trim())) {
                    nameString += (dock.toString() + "\n");
                }
            }
            for (Ship ship : seaPort.getShips()) {
                if (ship.getName().equalsIgnoreCase(name.trim())) {
                    nameString += (ship.toString() + "\n");
                }
            }
            for (Person person : seaPort.getPeople()) {
                if (person.getName().equalsIgnoreCase(name.trim())) {
                    nameString += (person.toString() + "\n");
                }
            }
        }
        System.out.println("Name String: " + nameString);
        return nameString;
    }

    //Sort-Related Methods
    public void sortBySeaPortName(ArrayList<SeaPort> thingList) {
        Collections.sort(thingList, this.getComparator());
    }
    
    public void sortByName() {
        sortBySeaPortName(ports);
        for(SeaPort seaport : ports) {
            seaport.sortByDockName(seaport.getDocks());
            seaport.sortByShipName(seaport.getQue());
            seaport.sortByShipName(seaport.getShips());
            seaport.sortByPersonName(seaport.getPeople());
        }
    }
    
    public void sortByShipLength() {
        for(SeaPort seaport : ports) {
            seaport.sortShipsByLength(seaport.getShips());
            seaport.sortShipsByLength(seaport.getQue());
        }
    }
    
    public void sortByShipWidth() {
        for(SeaPort seaport : ports) {
            seaport.sortShipsByWidth(seaport.getShips());
            seaport.sortShipsByWidth(seaport.getQue());
        }
    }
    
    public void sortByShipDraft() {
        for(SeaPort seaport : ports) {
            seaport.sortShipsByDraft(seaport.getShips());
            seaport.sortShipsByDraft(seaport.getQue());
        }
    }
    
    public void sortByShipWeight() {
        for(SeaPort seaport : ports) {
            seaport.sortShipsByWeight(seaport.getShips());
            seaport.sortShipsByWeight(seaport.getQue());
        }
    }

    //Search-Related Methods
    //method for searching everything by index
    public String searchIndex(int x) {
        String nameString = "";
        for (SeaPort seaPort : ports) {
            if (seaPort.getIndex() == x) {
                nameString += (seaPort.toString() + "\n");
            }
            for (Dock dock : seaPort.getDocks()) {
                if (dock.getIndex() == x) {
                    nameString += (dock.toString() + "\n");
                }
            }
            for (Ship ship : seaPort.getShips()) {
                if (ship.getIndex() == x) {
                    nameString += (ship.toString() + "\n");
                }
            }
            for (Person person : seaPort.getPeople()) {
                if (person.getIndex() == x) {
                    nameString += (person.toString() + "\n");
                }
            }
        }
        System.out.println("Name String: " + nameString);
        return nameString;
    }

    //method for searching everything by parent
    public String searchParent(int x) {
        String nameString = "";
        for (SeaPort seaPort : ports) {
            if (seaPort.getParent() == x) {
                nameString += (seaPort.toString() + "\n");
            }
            for (Dock dock : seaPort.getDocks()) {
                if (dock.getParent() == x) {
                    nameString += (dock.toString() + "\n");
                }
            }
            for (Ship ship : seaPort.getShips()) {
                if (ship.getParent() == x) {
                    nameString += (ship.toString() + "\n");
                }
            }
            for (Person person : seaPort.getPeople()) {
                if (person.getParent() == x) {
                    nameString += (person.toString() + "\n");
                }
            }
        }
        System.out.println("Name String: " + nameString);
        return nameString;
    }

    //method for searching all people by skill
    public String searchSkills(String name) {
        String nameString = "";
        for (SeaPort seaPort : ports) {
            for (Person person : seaPort.getPeople()) {
                if (person.getSkill().equalsIgnoreCase(name.trim())) {
                    nameString += (person.toString() + "\n");
                }
            }
        }
        System.out.println("Name String: " + nameString);
        return nameString;
    }
    
    public JPanel getAllJobs() {
        return jobsPanel;
    }

}

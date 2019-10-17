//File: Job.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Empty Job class for use in later projects
package seaportprogram;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class Job extends Thing implements Runnable {

    //Declare Variables
    //private static Random rn = new Random();
    private JPanel parent;
    //private Person worker = null;
    private int jobIndex;
    private long jobTime;
    private String jobName = "";
    private int parentIndex;
    private JProgressBar jobProgress = new JProgressBar();
    private boolean jobStopped = false;
    private boolean notCanceled = true;
    private JButton stopJob = new JButton("Stop");
    private JButton cancelJob = new JButton("Cancel");
    private Status status = Status.SUSPENDED;
    private volatile boolean atDock = false;
    private boolean isDone = false;
    private Ship parentShip;
    private SeaPort parentSeaPort;
    private volatile Dock parentDock;
    private ArrayList<String> requirements = new ArrayList<>();
    private int totalSkillsRequired = requirements.size();
    private int skillsMet = 0;
    private boolean metFlag = false;
    private boolean gotWorker = false;
    private StringBuilder missingSkills = new StringBuilder(" -");
    private ArrayList<Person> jobCandidates = new ArrayList<>();
    private ArrayList<Person> workers = new ArrayList<>();
    private long randomTime = ThreadLocalRandom.current().nextLong(25000);

    enum Status {
        RUNNING, SUSPENDED, WAITING, DONE, IMPOSSIBLE, WAITINGRESOURCES
    };

    public Job(ArrayList<SeaPort> allSeaPorts, JPanel panel, Scanner sc) {
        super(sc);
        parent = panel;
        jobName = this.getName();
        jobIndex = this.getIndex();
        parentIndex = this.getParent();
        for (SeaPort port : allSeaPorts) {
            for (Ship ship : port.getShips()) {
                if (ship.getIndex() == parentIndex) {
                    parentShip = ship;
                    parentSeaPort = port;
                    for (Dock dock : port.getDocks()) {
                        if (ship.getParent() == dock.getIndex()) {
                            atDock = true;
                            parentDock = dock;
                        }

                    }
                }
            }
        }
        jobTime = (long) sc.nextDouble();
        while (sc.hasNext()) {
            requirements.add(sc.next());
        }
        jobProgress = new JProgressBar();
        jobProgress.setStringPainted(true);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 40;
        parent.add(jobProgress, c);
        c.gridx = 2;
        parent.add(new JLabel((jobName + " on Ship " + parentShip.getName()), SwingConstants.CENTER), c);
        c.gridx = 3;
        parent.add(stopJob, c);
        c.gridx = 4;
        parent.add(cancelJob, c);
        stopJob.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jobStopGo();
                if (jobStopped) {
                    showStatus(Status.SUSPENDED);
                } else {
                  showStatus(Status.SUSPENDED);
                  stopJob.setText("Resuming - On Hold");
                }
            }
        });

        cancelJob.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setCanceled();
            }
        });
        new Thread(this).start();
    } // end constructor 

    public void jobStopGo() {
        jobStopped = !jobStopped;
    } // end method jobStopGo    

    public synchronized void releaseWorkers() {
        for (Person psn : workers) {
            psn.doneWorking();
            psn.setParent(parentSeaPort.getIndex());
            if (!jobCandidates.contains(psn)) {
                jobCandidates.add(psn);
            }
        }
        workers.clear();
    }

    public void releaseWorkersForever() {
        releaseWorkers();
        jobCandidates.clear();
    }

    public void setCanceled() {
        notCanceled = false;
        cancelJob.setBackground(Color.red);
        cancelJob.setText("Canceled");
        stopJob.setBackground(Color.red);
        stopJob.setText("Canceled");
        isDone = true;
        if (parentDock != null) {
                    shipJobsComplete(parentDock);
                }
        releaseWorkersForever();
    } // end setCanceled  

    void showStatus(Status st) {
        status = st;
        switch (status) {
            case IMPOSSIBLE:
                if (parentDock != null) {
                    shipJobsComplete(parentDock);
                }
                setCanceled();
                stopJob.setBackground(Color.gray);
                stopJob.setText("Impossible" + missingSkills);
                break;
            case SUSPENDED:
                stopJob.setBackground(Color.yellow);
                stopJob.setText("Suspended");
                releaseWorkers();
                break;
            case WAITING:
                stopJob.setBackground(Color.orange);
                stopJob.setText("Waiting for Dock in SeaPort " + parentSeaPort.getName());
                releaseWorkers();
                break;
            case WAITINGRESOURCES:
                stopJob.setBackground(Color.yellow);
                stopJob.setText("At Dock in Seaport " + parentSeaPort.getName() + " Awaiting Resources: " + requirements.toString());
                releaseWorkers();
                break;
            case RUNNING:
                StringBuffer workerString = new StringBuffer(" - ");
                for (Person worker : workers) {
                    workerString.append(worker.getName()).append(" is working. ");
                    worker.isWorking();
                }
                stopJob.setBackground(Color.green);
                stopJob.setText("Running in SeaPort " + parentSeaPort.getName() + workerString.append(requirements.toString()));
                break;
            case DONE:
                stopJob.setBackground(Color.red);
                stopJob.setText("Done");
                isDone = true;
                releaseWorkersForever();
                if (parentDock != null) {
                    shipJobsComplete(parentDock);
                }
                break;
        } // end switch on status
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        long startTime = time;
        long stopTime = time + 10 * jobTime;
        double duration = stopTime - time;
        boolean isPossible = this.isPossible();
        if (isPossible) {
            synchronized (parentSeaPort.getPeople()) {
                while (!atDock && notCanceled) {
                        showStatus(Status.WAITING);
                    try {
                        parentSeaPort.getPeople().wait();
                    } catch (InterruptedException e) {

                    }
                }
                
                while (!workersAvailable() && notCanceled) {
                        showStatus(Status.WAITINGRESOURCES);
                    try {
                        parentSeaPort.getPeople().wait();
                    } catch (InterruptedException e) {

                    }
                }
            }

                while (time < stopTime && notCanceled) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    if (jobStopped) {
                        showStatus(Status.SUSPENDED);
                    } else {
                        showStatus(Status.RUNNING);
                        time += 10;
                        jobProgress.setValue((int) (((time - startTime) / duration) * 100));
                    }

                }
                jobProgress.setValue(100);
                showStatus(Status.DONE);
                if (parentDock != null) {
                    shipJobsComplete(parentDock);
                }

                synchronized (parentSeaPort.getPeople()) {
                    parentSeaPort.getPeople().notifyAll();
                }
            }else {
            showStatus(Status.IMPOSSIBLE);
            if (parentDock != null) {
                    shipJobsComplete(parentDock);
                }
        }
        }

    

    public boolean workersAvailable() {
        System.out.println("Job Candidates: " + jobCandidates.toString());
        boolean workerAvailable = false;
        for (String skill : requirements) {
            for (Person psn : jobCandidates) {
                //synchronized (psn) {
                if (psn.hasSkill(skill) && !psn.getWorking() && !workerAvailable) {
                    workerAvailable = true;
                    if (!workers.contains(psn)) {
                        workers.add(psn);
                        psn.isWorking();
                    }
                }
                //}
            }
            if (!workerAvailable) {
                //for (Person psn : jobCandidates) {
                //    synchronized (psn) {
                //        psn.notifyAll();
                //    }
                //}
                System.out.println("returning false: " + workers.toString() + " removed");
                releaseWorkers();
                return false;
            }
            workerAvailable = false;
        }
        System.out.println("returning true");
        return true;
    }

    public boolean isPossible() {
        boolean requirementsMet = false;
        for (String skill : requirements) {
            for (Person psn : parentSeaPort.getPeople()) {
                if (psn.hasSkill(skill)) {
                    if (!jobCandidates.contains(psn)) {
                        requirementsMet = true;
                        jobCandidates.add(psn);
                    }
                }
            }
            if (requirementsMet == false) {
                missingSkills.append(" No ").append(skill).append(".");
                return false;
            }
            requirementsMet = false;
        }
        return true;
    }

    /* void showStatus(Status st) {
        status = st;
        switch (status) {
            case RUNNING:
                if (parentDock != null) {
                StringBuffer workerString = new StringBuffer(" - ");
                for (Person worker : workers) {
                    workerString.append(worker.getName()).append(" is working. ");
                    worker.isWorking("");
                }
                stopJob.setBackground(Color.green);
                stopJob.setText("Running in SeaPort " + parentSeaPort.getName() + workerString.append(requirements.toString()));
                } else {
                    showStatus(Status.WAITING);
                }
                break;
            case SUSPENDED:
                stopJob.setBackground(Color.yellow);
                stopJob.setText("Suspended");
                releaseWorkers();
                break;
            case WAITING:
                stopJob.setBackground(Color.orange);
                stopJob.setText("Waiting for Dock in SeaPort " + parentSeaPort.getName());
                releaseWorkers();
                break;
            case WAITINGRESOURCES:
                stopJob.setBackground(Color.yellow);
                stopJob.setText("At Dock in Seaport " + parentSeaPort.getName() + " Awaiting Resources: " + requirements.toString());
                releaseWorkers();
                break;
            case DONE:
                stopJob.setBackground(Color.red);
                stopJob.setText("Done");
                isDone = true;
                if (parentDock != null) {
                    shipJobsComplete(parentDock);
                }
                releaseWorkersForever();
                break;
            case IMPOSSIBLE:
                stopJob.setBackground(Color.gray);
                stopJob.setText("Impossible" + missingSkills);
                isDone = true;
                if (parentDock != null) {
                    shipJobsComplete(parentDock);
                }
                setCanceled();
                releaseWorkersForever();
                break;
        } // end switch on status
    } // end showStatus 

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        long startTime = time;
        long stopTime = time + 1000 * jobTime;
        double duration = stopTime - time;
        boolean isPossible = this.isPossible();
        if (isPossible) {
            for (Person psn : jobCandidates) {
                synchronized (psn) {
                    boolean workersAvailable = haveAvailability(jobCandidates);
                    boolean docked = atDock;
                    while (!workersAvailable || !docked) {
                        if (!docked) {
                            showStatus(Status.WAITING);
                        } else {
                            showStatus(Status.WAITINGRESOURCES);
                        }
                        try {
                            psn.wait();
                        } catch (InterruptedException e) {
                        } // end try/catch block
                        workersAvailable = haveAvailability(jobCandidates);
                        docked = atDock;
                    }// end while waiting for workers and a dock to be free     
                } // end sychronized 
            }

            while (time < stopTime && notCanceled) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                if (!jobStopped) {
                    showStatus(Status.RUNNING);
                    time += 100;
                    jobProgress.setValue((int) (((time - startTime) / duration) * 100));
                } else {
                    showStatus(Status.SUSPENDED);
                } // end if stepping     
            } // end runninig 
            jobProgress.setValue(100);
            showStatus(Status.DONE);

            for (Person psn : jobCandidates) {
                synchronized (psn) {
                    parentSeaPort.getPeople().notifyAll();
                }
            }
        } else {
            showStatus(Status.IMPOSSIBLE);
        }
    } // end method run - implements runnable */
    public void shipJobsComplete(Dock dock) {
        if (parentShip.jobsComplete()) {
            parentSeaPort.dockNext(dock);
            parentShip.setParent(parentSeaPort.getIndex());
        }  
    }

    public boolean getDone() {
        return isDone;
    }

    public String toString() {
        String sr = String.format("j:%7d:%15s:%7d", jobIndex, jobName, jobTime);
        return sr;
    } //end method toString

    public JPanel getPanel() {
        return parent;
    }

    public void setParentDock(Dock dock) {
        parentDock = dock;
        this.setParent(dock.getIndex());
        parentIndex = dock.getIndex();
        atDock = true;
        System.out.println(parentShip.getName() + " has docked at " + parentDock.getName() + ". " + this.getName() + " can now execute.");
    }

    /* public boolean isPossible() {
        totalSkillsRequired = requirements.size();
        skillsMet = 0;
        for (String skill : requirements) {
            metFlag = false;
            for (Person psn : parentSeaPort.getPeople()) {
                if ((psn.hasSkill(skill)) && (!jobCandidates.contains(psn))) {
                    if (metFlag == false) {
                        skillsMet++;
                    }
                    metFlag = true;
                    jobCandidates.add(psn);
                }
            }
            if (metFlag == false) {
                missingSkills.append(" No ").append(skill).append(".");
            }
        }
        if (totalSkillsRequired == skillsMet) {
            return true;
        } else {
            showStatus(Status.IMPOSSIBLE);
            return false;
        }
    }*/
 /*public boolean haveAvailability(ArrayList<Person> possibleWorkers) {
        int totalRequirements = requirements.size();
        int requirementsMet = 0;
        boolean notMet;
        releaseWorkers();
        for (String skill : requirements) {
            notMet = true;
            for (Person psn : possibleWorkers) {
                if (!psn.getWorking() && notMet) {
                    if (!workers.contains(psn)) {
                        requirementsMet++;
                        workers.add(psn);
                    }
                    notMet = false;
                }
            }
        }
        if (totalRequirements == requirementsMet) {
            return true;
        } else {
            releaseWorkers();
            return false;
        }
    }*/
}

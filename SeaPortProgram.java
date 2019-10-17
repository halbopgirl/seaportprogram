//File: SeaPortProgram.java
//Name: Haleigh Jayde Doetschman
//Date: 1/22/2019
//Class: CMSC 335 Spring 2019
//Purpose: Defines the GUI and contains main method
package seaportprogram;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

public class SeaPortProgram extends JFrame {

    //variables
    private World world;
    private final JLabel fileNameLabel = new JLabel("File: ");
    private final JLabel searchFileLabel = new JLabel("Search For: ");
    private final JLabel searchByLabel = new JLabel("Search By: ");
    private final JLabel sortByLabel = new JLabel("Sort By: ");
    private final String[] options = {"Name", "Index", "Parent", "Skill"};
    private final String[] sortOptions = {"Name", "Ship Weight", "Ship Length", "Ship Width", "Ship Draft"};
    private final JComboBox dropDown = new JComboBox(options);
    private final JComboBox sortDropDown = new JComboBox(sortOptions);
    private JTextArea fileText = new JTextArea();
    private final JScrollPane scroller = new JScrollPane(fileText);
    private JTextField fileNameBox = new JTextField("Enter File Name Here");
    private JTextField searchFileBox = new JTextField("Enter Search Query");
    private JTextArea searchResultsBox = new JTextArea();
    private final JScrollPane scroller2 = new JScrollPane(searchResultsBox);
    private final JButton chooseButton = new JButton("Choose...");
    private final JButton searchButton = new JButton("Search");
    private final JButton sortButton = new JButton("Sort");
    private final int gap = 20;
    private final JFileChooser fileChooser = new JFileChooser(".");
    private File fileChosen;
    private JTabbedPane tabs = new JTabbedPane();
    private JTree tree;
    private final JScrollPane scroller3 = new JScrollPane();
    private final JTextArea jobProgress = new JTextArea();
    private final JScrollPane scroller4 = new JScrollPane(jobProgress);
    
    
    //JPanel with
    //JProgressBars to display progress of each job
    //JButtons on job panel allow job to be suspended or cancelled
    
    //create simple GUI that presents data in the structures with some buttons
    //and text fields supporting searching on the various fields of each class
    //create interface
    public SeaPortProgram() {
        
        //tab 1 panel
        JPanel tab1 = new JPanel();
        tab1.setLayout(new BorderLayout());
        tab1.add(scroller, BorderLayout.CENTER);
        JPanel tab1Inset = new JPanel();
        tab1Inset.setLayout(new BorderLayout());
        tab1Inset.add(sortByLabel, BorderLayout.WEST);
        tab1Inset.add(sortDropDown, BorderLayout.CENTER);
        tab1Inset.add(sortButton, BorderLayout.EAST);
        tab1.add(tab1Inset, BorderLayout.SOUTH);
        
        
        //tab 2 panel
        JPanel tab2 = new JPanel();
        tab2.setLayout(new BorderLayout());
        tab2.add(scroller3, BorderLayout.CENTER);
        
        
        //tab 3 panel
        JPanel tab3 = new JPanel();
        tab3.add(searchFileLabel);
        tab3.add(searchFileBox);
        tab3.add(searchButton);
        tab3.add(searchButton);
        tab3.add(searchByLabel);
        tab3.add(dropDown);
        tab3.add(scroller2);  
        GroupLayout tab3Layout = new GroupLayout(tab3);
        tab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(tab3Layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGap(20, 20, 20)
                .addGroup(tab3Layout.createSequentialGroup()
                        .addComponent(searchFileLabel)
                        .addContainerGap(gap, 1000)
                        .addComponent(searchFileBox)
                        .addContainerGap(gap, 1000)
                        .addComponent(searchButton))
                .addGap(20, 20, 20)
                .addGroup(tab3Layout.createSequentialGroup()
                        .addComponent(searchByLabel)
                        .addContainerGap(gap, 1000)
                        .addComponent(dropDown, 20, 100, 200))
                .addGap(20, 20, 20)
                .addComponent(scroller2, 400, 400, 400)
                .addGap(20, 20, 20)
        );
        tab3Layout.setVerticalGroup(tab3Layout.createSequentialGroup()
                .addGap(20)
                .addGroup(tab3Layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(searchFileLabel)
                        .addComponent(searchFileBox)
                        .addComponent(searchButton)
                )
                .addGap(20)
                .addGroup(tab3Layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(searchByLabel)
                        .addComponent(dropDown)
                )
                .addGap(20)
                .addComponent(scroller2)
                .addGap(20)
        );
        
        //tab 4 panel
        JPanel tab4 = new JPanel();
        tab4.setLayout(new BorderLayout());
        tab4.add(scroller4, BorderLayout.CENTER);
        
        //add tabs to TabbedPane
        tabs.addTab("The World", tab1);
        tabs.addTab("Tree View", tab2);
        tabs.addTab("Search Results", tab3);
        tabs.addTab("Job Progress", tab4);

        //big panel
        JPanel guiPanel = new JPanel();
        GroupLayout layout = (new GroupLayout(guiPanel));
        guiPanel.setLayout(layout);
        guiPanel.add(fileNameLabel);
        guiPanel.add(tabs);
        guiPanel.add(fileNameBox);
        guiPanel.add(chooseButton);
        layout.setHorizontalGroup(layout
                .createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGap(20, 20, 20)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(fileNameLabel)
                        .addContainerGap(gap, 1000)
                        .addComponent(fileNameBox)
                        .addContainerGap(gap, 1000)
                        .addComponent(chooseButton, 150, 150, 150))
                .addGap(20, 20, 20)
                .addComponent(tabs)
                .addGap(20, 20, 20)
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(fileNameLabel)
                        .addComponent(fileNameBox)
                        .addComponent(chooseButton)
                )
                .addGap(20)
                .addComponent(tabs)
                .addGap(20)
        );

        //frame for panel
        JFrame panelFrame = new JFrame("SeaPort Program");
        panelFrame.add(guiPanel);
        panelFrame.setSize(800, 650);
        panelFrame.setVisible(true);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        guiPanel.setBorder(padding);
        panelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Refine Panel Elements
        Dimension searchBoxSize = new Dimension(1000, 1000);
        scroller2.setPreferredSize(searchBoxSize);
        /*fileText.setEditable(false);
        searchResultsBox.setEditable(false);
        fileNameBox.setSize(300, 50);
        scroller2.setSize(500, 100);
        Dimension sd = scroller2.getSize();
        scroller.setPreferredSize(sd);
        scroller.setMinimumSize(sd);
        scroller2.setPreferredSize(sd);
        scroller2.setMinimumSize(sd);
        Dimension d = fileNameBox.getSize();
        fileNameBox.setPreferredSize(d);
        fileNameBox.setMaximumSize(d);
        fileNameBox.setMinimumSize(d);
        searchFileBox.setSize(300, 50);
        Dimension d2 = searchFileBox.getSize();
        searchFileBox.setPreferredSize(d2);
        searchFileBox.setMaximumSize(d2);
        searchFileBox.setMinimumSize(d2);
        Dimension d3 = chooseButton.getSize();
        searchButton.setPreferredSize(d3);
        searchButton.setMaximumSize(d3);
        searchButton.setMinimumSize(d3);
        sortButton.setPreferredSize(d3);
        sortButton.setMaximumSize(d3);
        sortButton.setMinimumSize(d3);
        Dimension d4 = searchFileLabel.getSize();
        fileNameLabel.setPreferredSize(d4);
        fileNameLabel.setMaximumSize(d4);
        fileNameLabel.setMinimumSize(d4);
        Dimension d5 = dropDown.getSize();
        dropDown.setPreferredSize(d5);
        dropDown.setMaximumSize(d5);
        dropDown.setMinimumSize(d5);
        sortDropDown.setPreferredSize(d5);
        sortDropDown.setMaximumSize(d5);
        sortDropDown.setMinimumSize(d5);
        Dimension d6 = scroller.getSize();
        scroller2.setMinimumSize(d6); */

        //action listeners for clicking in boxes
        fileNameBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileNameBox.setText("");
            }
        });
        searchFileBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchFileBox.setText("");
            }
        });

        //action listeners for buttons
        chooseButton.addActionListener((ActionEvent f) -> {
            fileText.setText("");
            JFrame openFrame = new JFrame("Open File");
            int returnVal = fileChooser.showOpenDialog(openFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileChosen = fileChooser.getSelectedFile();
                fileNameBox.setText(fileChosen.getPath());
                world = new World("World", 0, 0, fileChosen);
                fileText.setText(world.toString());
                //use JTree to display the contents of the file
                tree = createTree(world);
                scroller3.getViewport().add(tree);
                scroller4.getViewport().add(world.getAllJobs());
            } else {
                JOptionPane.showMessageDialog(null, "No File Selected");
            }
            System.out.println(returnVal);
        });

        sortButton.addActionListener((ActionEvent f) -> {
            String selected = (String) sortDropDown.getSelectedItem();
            try {
                switch (selected) {
                    case "Name":
                        world.sortByName();
                        fileText.setText(world.sortedString());
                        break;
                    case "Ship Weight":
                        world.sortByShipWeight();
                        fileText.setText(world.sortedString());
                        break;
                    case "Ship Length":
                        world.sortByShipLength();
                        fileText.setText(world.sortedString());
                        break;
                    case "Ship Width":
                        world.sortByShipWidth();
                        fileText.setText(world.sortedString());
                        break;
                    case "Ship Draft":
                        world.sortByShipDraft();
                        fileText.setText(world.sortedString());
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Not a valid input");
            }
        });

        searchButton.addActionListener((ActionEvent f) -> {
            String selected = (String) dropDown.getSelectedItem();
            String query = searchFileBox.getText().trim();
            System.out.println(query);
            String output = "";
            try {
                switch (selected) {
                    case "Name":
                        searchResultsBox.setText(world.searchNames(query));
                        break;
                    case "Index":
                        int x = Integer.parseInt(query);
                        searchResultsBox.setText(world.searchIndex(x));
                        break;
                    case "Parent":
                        x = Integer.parseInt(query);
                        searchResultsBox.setText(world.searchParent(x));
                        break;
                    case "Skill":
                        searchResultsBox.setText(world.searchSkills(query));
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Not a valid input");
            }
        });
    }
    
    public JTree createTree(World world) {
        DefaultMutableTreeNode worldNode = new DefaultMutableTreeNode("World");
        JTree worldTree = new JTree(worldNode);
        DefaultMutableTreeNode portsNode = new DefaultMutableTreeNode("Ports");
        worldNode.add(portsNode);
        
        for (SeaPort port : world.getPorts()) {
            DefaultMutableTreeNode thisPortNode = new DefaultMutableTreeNode(port.getName());
            portsNode.add(thisPortNode);
            DefaultMutableTreeNode thisPortDocks = new DefaultMutableTreeNode("Docks");
            DefaultMutableTreeNode thisPortShips = new DefaultMutableTreeNode("Ships");
            DefaultMutableTreeNode thisPortPeople = new DefaultMutableTreeNode("People");
            thisPortNode.add(thisPortDocks);
            thisPortNode.add(thisPortShips);
            thisPortNode.add(thisPortPeople);
            
            for (Dock someDock : port.getDocks()) {
                DefaultMutableTreeNode aDock = new DefaultMutableTreeNode(someDock.getName());
                thisPortDocks.add(aDock);
            }
            
            for (Ship someShip : port.getShips()) {
                DefaultMutableTreeNode aShip = new DefaultMutableTreeNode(someShip.getName());
                thisPortShips.add(aShip);
            }
            
            for (Person somePerson : port.getPeople()) {
                DefaultMutableTreeNode aPerson = new DefaultMutableTreeNode(somePerson.getName());
                thisPortPeople.add(aPerson);
            }
        }
        return worldTree;
    }

    //main method
    public static void main(String[] args) {
        SeaPortProgram project1 = new SeaPortProgram();
    }

}

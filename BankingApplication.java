package com.banking_application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BankingApplication{
    private static Map<Integer,SavingsAccount> customers = new HashMap<>();
    private static ArrayList<String> customerNames=new ArrayList<>();
    private static CardLayout cardLayout = new CardLayout();
    private static JPanel mainPanel = new JPanel(cardLayout);
    private static JLabel resultLabel = new JLabel("");

    private static DefaultListModel<String> listModel = new DefaultListModel<>();
    private static JList<String> customerList = new JList<>(listModel);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> createUI());
    }

    private static void createUI() {
        JFrame frame = new JFrame("Bank Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 450);

        mainPanel.add(createMainMenuPanel(), "menu");
        mainPanel.add(createSetInterestPanel(), "interest");
        mainPanel.add(createCustomerListPanel(), "customers");
        mainPanel.add(createAddCustomerPanel(), "addCustomer");
        mainPanel.add(howBankWorks(), "howBankWorks");
        mainPanel.add(aboutOurTeam(), "about");
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createMainMenuPanel() {
    	Color customColor = new Color(0,102,204);
    	JPanel mainPanelLayout = new JPanel(new BorderLayout());
    	
    	
        JLabel headerLabel = new JLabel("BANK OF COGNIZANT",JLabel.CENTER);     
        

        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setForeground(customColor);
        JPanel panel = new JPanel(new GridLayout(4, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton btnSetInterest = styledButton("Set Annual Interest Rate");
        JButton btnCustomers = styledButton("List of Account Holders");
        JButton btnWorks = styledButton("How Our Bank Works");
        JButton btnAbout = styledButton("About Our Team");
        btnSetInterest.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnCustomers.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnWorks.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnAbout.setFont(new Font("Segoe UI", Font.BOLD, 20));

        btnSetInterest.addActionListener(e -> cardLayout.show(mainPanel, "interest"));
        btnCustomers.addActionListener(e -> {
            refreshCustomerListPanel();
            resultLabel.setText("");
            cardLayout.show(mainPanel, "customers");
        });
        btnWorks.addActionListener(e -> cardLayout.show(mainPanel, "howBankWorks"));
        btnAbout.addActionListener(e -> cardLayout.show(mainPanel, "about"));

        panel.add(btnSetInterest);
        panel.add(btnCustomers);
        panel.add(btnWorks);
        panel.add(btnAbout);
        mainPanelLayout.add(headerLabel,BorderLayout.NORTH);
        
        mainPanelLayout.add(panel,BorderLayout.CENTER);

        return wrapWithBack(mainPanelLayout, false);
    }
    private static JPanel createSetInterestPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 30));

        JLabel label1 = styledLabel("Current annual Interest Rate is: " + SavingsAccount.getAnnualInterestRate() + "%.");
        label1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel label = styledLabel("Enter Annual Interest Rate (%):");
        label.setFont(new Font("Segoe UI", Font.LAYOUT_LEFT_TO_RIGHT, 20));

        JTextField interestField = styledTextField();
        JButton setButton = styledButton("Set");
        setButton.setFont(new Font("Segoe UI", Font.BOLD, 20));

        setButton.addActionListener(e -> {
            try {
                double annualInterestRate = Double.parseDouble(interestField.getText());
                if(annualInterestRate<1.0) {
                	JOptionPane.showMessageDialog(panel, "Interest Rate cannot be zero or negative.\nPlease enter a valid interest rate.","negative rate",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                SavingsAccount.modifyInterestRate(annualInterestRate);
                label1.setText("Current annual Interest Rate is: " + SavingsAccount.getAnnualInterestRate() + "%.");
                JOptionPane.showMessageDialog(panel, "Interest rate set to " + annualInterestRate + "%");
                interestField.setText("");
                } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid number.","valid",JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(label1);
        panel.add(label);
        panel.add(interestField);
        panel.add(setButton);

        return wrapWithBack(panel, true);
    }


    private static JPanel createCustomerListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = styledLabel("ACCOUNT HOLDERS LIST");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));

        customerList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(customerList);
        JButton addCustomerButton = styledButton("Create new account");

        addCustomerButton.addActionListener(e -> cardLayout.show(mainPanel, "addCustomer"));

        customerList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = customerList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        String selected = customerNames.get(index);
                        int accountNumber = Integer.parseInt(selected.substring(selected.lastIndexOf(" ")+1));
                        String[] options = {"Show Balance", "Calculate Interest", "Update balance"};
                        int choice = JOptionPane.showOptionDialog(
                                panel,
                                "Choose an action for " + selected.substring(0,selected.indexOf(" ")),
                                "Customer Action",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                options,
                                options[0]
                        );
                        if (choice == 0) {
                            JOptionPane.showMessageDialog(panel,
                            	selected.substring(0,selected.indexOf(" ")) + "'s Balance: ₹" + String.format("%.2f", customers.get(accountNumber).getSavingsBalance()),
                                "Balance Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else if (choice == 1) {
                            if (SavingsAccount.getAnnualInterestRate() == 0.0) {
                                JOptionPane.showMessageDialog(panel,
                                    "Annual interest rate is not set. Please set it first.",
                                    "Interest Rate Missing",
                                    JOptionPane.WARNING_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(panel,
                                    "Current Balance for " + selected.substring(0,selected.indexOf(" ")) + ": ₹" + String.format("%.2f", customers.get(accountNumber).getSavingsBalance()) +
                                    "\nInterest for " + selected.substring(0,selected.indexOf(" ")) + ": ₹" + String.format("%.2f", customers.get(accountNumber).calculateMonthlyInterestWithoutUpdate()),
                                    "Interest Info",
                                    JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else if (choice == 2) {
                            if (SavingsAccount.getAnnualInterestRate() == 0.0) {
                                JOptionPane.showMessageDialog(panel,
                                	"Can't update balance as Annual Interest is set to 0",
                                    "No Interest Update",
                                    JOptionPane.WARNING_MESSAGE);
                            } else {
                                customers.get(accountNumber).calculateMonthlyInterest();
                                JOptionPane.showMessageDialog(panel,
                                    "Balance Updated!\nNew Balance with interest for " + selected.substring(0,selected.indexOf(" ")) + ": ₹" + String.format("%.2f", customers.get(accountNumber).getSavingsBalance()),
                                    "Balance with Interest",
                                    JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }
            }
        });


        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(addCustomerButton, BorderLayout.SOUTH);

        JPanel wrapped = wrapWithBack(panel, true);
        wrapped.add(resultLabel, BorderLayout.SOUTH);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        return wrapped;
    }

    private static JPanel createAddCustomerPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JTextField nameField = styledTextField();
        JTextField balanceField = styledTextField();
        JButton addButton = styledButton("Add Account");

        panel.add(styledLabel("Account Holder Name:"));
        panel.add(nameField);
        panel.add(styledLabel("Balance:"));
        panel.add(balanceField);
        panel.add(new JLabel(""));
        panel.add(addButton);

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Name cannot be empty.\nPlease enter a Name","name required",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                else if(!name.matches("[a-zA-Z\s]+")){
                	JOptionPane.showMessageDialog(panel, "Name can contain Alphabets and Spaces Only.\nEnter a valid Name.","alphabets",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (balanceField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Balance cannot be empty.\nPlease enter your balance","balance required",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                double balance = Double.parseDouble(balanceField.getText().trim());

                
                if(balance<1.0) {
                	JOptionPane.showMessageDialog(panel, "Balance cannot be zero or negative.\nPlease enter a valid balance.","negative balance",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                SavingsAccount ac=new SavingsAccount(name,balance); 
                customerNames.add(name+" - "+ac.getAccountNumber());
                customers.put(ac.getAccountNumber(),ac);
                refreshCustomerListPanel();
                JOptionPane.showMessageDialog(panel, "Account Created Successfully!\nAccount Number : "+ac.getAccountNumber()+"\nAccount Holder Name : "+ name+"\nOpening Balance : ₹"+balance);
                nameField.setText("");
                balanceField.setText("");
                cardLayout.show(mainPanel, "customers");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid balance.","balance",JOptionPane.WARNING_MESSAGE);
            }
        });

        return wrapWithBack(panel, true);
    }
    
    
    private static JPanel howBankWorks() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("How Bank Works", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));


        JTextPane info = new JTextPane();
        info.setContentType("text/html");
        info.setText(
            "<html>" +
            "Welcome to the Bank of Cognizant! Our mission is to provide transparent and efficient banking services to our customers.<br><br>" +
            "Here's a brief overview of how we operate:<br><br>" +
            "<b>Annual Interest Rate:</b> Initially, the annual interest rate is set to 0. You set the rate of interest first to see the 'Interest' or to 'Update balance with Interest'. The interest rate is divided by 12 to calculate the monthly interest rate. This monthly interest rate is then multiplied by your account balance to determine the interest earned each month.<br><br>" +
            "<b>Interest Calculation:</b> Each month, the interest you will earn is shown, ensuring that your savings grow steadily over time.<br><br>" +
            "<b>Update Balance with Interest:</b> Our system allows you to update your balance with the interest earned for the month, providing an accurate and up-to-date account balance.<br><br>" +
            "We are committed to offering you the best banking experience with clear and straightforward processes." +
            "</html>"
        );
        info.setEditable(false);
        info.setPreferredSize(new Dimension(400, 400));

        JScrollPane scrollPane = new JScrollPane(info);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return wrapWithBack(panel, true);
    }




    
    
    private static JPanel aboutOurTeam() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("OUR TEAM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 23));

        JTextPane info = new JTextPane();
        info.setContentType("text/html");
        info.setText(
            "<html>" +
            "<b>Meet the team behind Ctrl+Alt+Defeat, the creators of the Bank of Cognizant:</b><br><br><br>" +
            "<b>Sankeerth</b><br><br>" +
            "<b>Sathya</b><br><br>" +
            "<b>Abhigyan</b><br><br>" +
            "<b>Rohit Mishra</b><br><br><br>" +
            "We are proud to present our project and hope you find our banking application useful and user-friendly. Thank you for choosing the Bank of Cognizant!" +
            "</html>"
            
        );
        info.setEditable(false);
        info.setPreferredSize(new Dimension(400, 400));

        JScrollPane scrollPane = new JScrollPane(info);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return wrapWithBack(panel, true);
    }


    private static void refreshCustomerListPanel() {
        listModel.clear();
        Collections.sort(customerNames);
        for (String s : customerNames) {
            listModel.addElement(s);
        }
    }

    private static JPanel wrapWithBack(JPanel panel, boolean addBack) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.CENTER);

        if (addBack) {
            JButton backButton = styledButton("Back");
            backButton.setPreferredSize(new Dimension(80, 30));
            backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

            JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            topBar.add(backButton);
            wrapper.add(topBar, BorderLayout.NORTH);
        }

        return wrapper;
    }

    // --- Styling Helpers ---

    private static JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(50, 40));
        return btn;
    }

    private static JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private static JTextField styledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }
 
}

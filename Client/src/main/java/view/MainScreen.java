package view;

import javax.swing.*;
import java.awt.*;

public class MainScreen {
    JLabel portLabel;
    JTextField portField;
    JLabel clientNameLabel;
    JTextField clientNameField;
    JLabel ipLabel;
    JTextField ipField;
    JButton createClientButton;
    JFrame registerClientFrame = new JFrame("Register Client");
    public MainScreen() {
        registerClientFrame();
    }

    private void registerClientFrame() {
        registerClientFrame.setSize(500, 200);
        registerClientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerClientFrame.setLayout(new GridLayout(4, 1));

        JPanel ipPanel = new JPanel();
        ipPanel.setLayout(new FlowLayout());
        ipLabel = new JLabel("IP: ");
        ipLabel.setFont(new Font("Serif", Font.BOLD, 20));
        ipField = new JTextField(20);
        ipField.setFont(new Font("Serif", Font.PLAIN, 15));
        ipPanel.add(ipLabel);
        ipPanel.add(ipField);
        registerClientFrame.add(ipPanel);

        JPanel portPanel = new JPanel();
        portPanel.setLayout(new FlowLayout());
        portLabel = new JLabel("Port: ");
        portLabel.setFont(new Font("Serif", Font.BOLD, 20));
        portField = new JTextField(5);
        portField.setFont(new Font("Serif", Font.PLAIN, 15));
        portPanel.add(portLabel);
        portPanel.add(portField);
        registerClientFrame.add(portPanel);

        JPanel nameClientPanel = new JPanel();
        nameClientPanel.setLayout(new FlowLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        clientNameLabel = new JLabel("Client name: ");
        clientNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        clientNameField = new JTextField(20);
        clientNameField.setFont(new Font("Serif", Font.PLAIN, 15));
        nameClientPanel.add(clientNameLabel);
        nameClientPanel.add(clientNameField);
        registerClientFrame.add(nameClientPanel);

        createClientButton = new JButton("CREATE");
        buttonPanel.add(createClientButton);
        registerClientFrame.add(buttonPanel);

        createClientButton.addActionListener(e -> {
            if (!checkInfo()) {
                JOptionPane.showMessageDialog(registerClientFrame, "Please enter valid information!");
                return;
            }
            registerClientFrame.setVisible(false);
            Thread t = new Thread(){
                public void run(){
                    try{
                        Thread.sleep(1000);
                        new ClientFrame(ipField.getText(), Integer.parseInt(portField.getText()), clientNameField.getText());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        JOptionPane.showConfirmDialog(null, "Error!");
                    }
                }
            };
            t.start();
        });

        registerClientFrame.setVisible(true);
    }


    private boolean checkInfo() {
        if (portField.getText().equals("") || clientNameField.getText().equals("") || ipField.getText().equals("")) {
            return false;
        }
        try {
            Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

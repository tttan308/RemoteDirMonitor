package view;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainScreen {

    JLabel portLabel;
    JTextField portField;
    JLabel serverNameLabel;
    JTextField serverNameField;
    JButton createServerButton;

    JFrame registerServerFrame = new JFrame("Register Server");
    JFrame serverFrame = new JFrame("Server");
    JLabel serverIP;
    public MainScreen() {
        registerServerFrame();
    }

    public void registerServerFrame() {
        registerServerFrame.setSize(500, 200);
        registerServerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerServerFrame.setLayout(new GridLayout(3, 1));

        JPanel portPanel = new JPanel();
        portPanel.setLayout(new FlowLayout());
        portLabel = new JLabel("Port: ");
        portLabel.setFont(new Font("Serif", Font.BOLD, 20));
        portField = new JTextField(5);
        portField.setFont(new Font("Serif", Font.PLAIN, 15));
        portPanel.add(portLabel);
        portPanel.add(portField);
        registerServerFrame.add(portPanel);

        JPanel nameServerPanel = new JPanel();
        nameServerPanel.setLayout(new FlowLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        serverNameLabel = new JLabel("Server name: ");
        serverNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        serverNameField = new JTextField(20);
        serverNameField.setFont(new Font("Serif", Font.PLAIN, 15));
        nameServerPanel.add(serverNameLabel);
        nameServerPanel.add(serverNameField);
        registerServerFrame.add(nameServerPanel);

        createServerButton = new JButton("CREATE");
        buttonPanel.add(createServerButton);
        registerServerFrame.add(buttonPanel);

        createServerButton.addActionListener(e -> {
            if (!checkInfo()) {
                JOptionPane.showMessageDialog(registerServerFrame, "Please enter valid information!");
                return;
            }
            registerServerFrame.setVisible(false);
            serverFrame();
        });

        registerServerFrame.setVisible(true);
    }

    void serverFrame() {
        serverFrame.setSize(500, 500);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setLayout(new GridLayout(2, 1));

        JPanel infoServer = new JPanel();
        infoServer.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
        JLabel serverName = new JLabel("Server name: " + serverNameField.getText());
        JLabel portName = new JLabel("Port: " + portField.getText());
        try {
            serverIP = new JLabel("Server IP: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        infoServer.add(serverName);
        infoServer.add(portName);
        infoServer.add(serverIP);
        serverFrame.add(infoServer);

        serverFrame.setVisible(true);
    }

    public boolean checkInfo() {
        if (portField.getText().equals("") || serverNameField.getText().equals("")) {
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

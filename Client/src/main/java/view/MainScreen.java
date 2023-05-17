package view;

import controller.ClientHandle;
import model.Client;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MainScreen {
    JLabel portLabel;
    JTextField portField;
    JLabel clientNameLabel;
    JTextField clientNameField;
    JLabel ipLabel;
    JTextField ipField;
    JButton createClientButton;
    public static JFrame registerClientFrame = new JFrame("Register Client");
    public static ClientHandle clientHandle = null;
    public static StringBuilder msgRcv = new StringBuilder("Connecting to server...");
    public static JTextArea msgField = new JTextArea(10, 50);
    public static JButton disButton = new JButton("DISCONNECT");
    public static JFrame clientFrame = new JFrame();
    public MainScreen() {
        registerClientFrame();
    }

    private void registerClientFrame() {
        registerClientFrame.setSize(500, 230);
        registerClientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerClientFrame.setLayout(new BorderLayout());
        registerClientFrame.setLocationRelativeTo(null);

        JPanel registerPanel = new JPanel();
        registerPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Register Client")));
        registerPanel.setLayout(new GridLayout(3, 2, 10, 10));

        ipLabel = new JLabel("IP: ");
        ipLabel.setFont(new Font("Serif", Font.BOLD, 20));
        ipField = new JTextField(20);
        ipField.setFont(new Font("Serif", Font.PLAIN, 15));
        registerPanel.add(ipLabel);
        registerPanel.add(ipField);

        portLabel = new JLabel("Port: ");
        portLabel.setFont(new Font("Serif", Font.BOLD, 20));
        portField = new JTextField(5);
        portField.setFont(new Font("Serif", Font.PLAIN, 15));
        registerPanel.add(portLabel);
        registerPanel.add(portField);

        clientNameLabel = new JLabel("Client name: ");
        clientNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        clientNameField = new JTextField(20);
        clientNameField.setFont(new Font("Serif", Font.PLAIN, 15));
        registerPanel.add(clientNameLabel);
        registerPanel.add(clientNameField);

        registerClientFrame.add(registerPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        createClientButton = new JButton("CREATE");
        buttonPanel.add(createClientButton);
        registerClientFrame.add(buttonPanel, BorderLayout.SOUTH);

        registerClientFrame.setVisible(true);

        createClientButton.addActionListener(e -> {
            if (!checkInfo()) {
                JOptionPane.showMessageDialog(registerClientFrame, "Please enter valid information!");
                return;
            }
            Thread t = new Thread() {
                @Override
                public void run() {
                    clientHandle = new ClientHandle(new Client(ipField.getText(), Integer.parseInt(portField.getText()),clientNameField.getText()));
                    clientHandle.handle();
                }
            };
            t.start();
            new ClientFrame(ipField.getText(), portField.getText(),clientNameField.getText());
            registerClientFrame.setVisible(false);
        });

        disButton.addActionListener(e -> {
            clientHandle.closeSocket();
            registerClientFrame.setVisible(true);
            clientFrame.setVisible(false);
        });

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

package view;

import controller.ClientHandle;
import model.Client;

import javax.swing.*;
import java.awt.*;

public class ClientFrame {
    public ClientFrame(String ip, int port, String name) {
        Client c = new Client(ip, port, name);
        ClientHandle clientHandle = new ClientHandle(c);

        JFrame clientFrame = new JFrame("Client " + name);
        clientFrame.setSize(800, 500);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setLayout(new GridLayout(3, 1));

        JPanel inforPanel = new JPanel();
        inforPanel.setLayout(new FlowLayout());
        JLabel ipLabel = new JLabel("IP: " + ip);
        ipLabel.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel portLabel = new JLabel("Port: " + port);
        portLabel.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel clientNameLabel = new JLabel("Client name: " + name);
        clientNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        inforPanel.add(ipLabel);
        inforPanel.add(portLabel);
        inforPanel.add(clientNameLabel);

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new FlowLayout());
        JTextArea msgField = new JTextArea(20, 70);
        msgField.setFont(new Font("Serif", Font.PLAIN, 15));
        msgField.setText("Connecting to server...");
        msgPanel.add(msgField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton sendButton = new JButton("DISCONNECT");
        buttonPanel.add(sendButton);

        clientFrame.add(inforPanel);
        clientFrame.add(msgPanel);
        clientFrame.add(sendButton);

        clientFrame.setVisible(true);
    }
}

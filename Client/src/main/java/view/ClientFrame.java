package view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static view.MainScreen.*;

public class ClientFrame {

    public ClientFrame(String ip, String port, String name) {
        clientFrame = new JFrame("Client " + name);
        clientFrame.setSize(1000, 500);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setLayout(new BorderLayout());

        JPanel inforPanel = new JPanel();
        inforPanel.setLayout(new FlowLayout());
        inforPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Client Information")));
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
        msgPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Message")));
        msgPanel.setLayout(new BorderLayout());
        msgField.setFont(new Font("Serif", Font.PLAIN, 20));
        msgField.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(msgField);
        msgPanel.add(scrollPane, BorderLayout.CENTER);



        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(disButton);
        //set size for button
        disButton.setPreferredSize(new Dimension(200, 50));
        disButton.setFont(new Font("Serif", Font.BOLD, 25));

        clientFrame.add(inforPanel, BorderLayout.NORTH);
        clientFrame.add(msgPanel, BorderLayout.CENTER);
        clientFrame.add(buttonPanel, BorderLayout.SOUTH);

        clientFrame.setLocationRelativeTo(null);
        clientFrame.setVisible(true);

        clientFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientHandle.closeSocket();
                registerClientFrame.setVisible(true);
                clientFrame.setVisible(false);
            }
        });
    }
}

package view;

import controller.ServerHandle;
import model.ClientList;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class MainScreen {

    JLabel portLabel;
    JTextField portField;
    JLabel serverNameLabel;
    JTextField serverNameField;
    JButton createServerButton;

    JFrame registerServerFrame = new JFrame("Register Server");
    JFrame serverFrame = new JFrame("Server");
    JLabel serverIP;

    JScrollPane clientListScrollPanel;
    JPanel clientListPanel;
    JScrollPane msgScrollPanel;
    JPanel msgPanel;
    public static JTextArea msgField = new JTextArea(10, 5);
    public static StringBuilder msg = new StringBuilder("Waiting for client...\n");
    public static DefaultListModel<String> clientListModel;
    JList<String> clientList;
    JPanel selectDirectoryPanel;
    JLabel selectDirectory;
    JLabel directoryPath;
    JButton selectDirectoryButton;

    Map <String, String> clientDirectory = new HashMap<>();

    public MainScreen() {
        registerServerFrame();
    }

    private void registerServerFrame() {
        registerServerFrame.setSize(500, 180);
        registerServerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerServerFrame.setLayout(new BorderLayout());
        registerServerFrame.setLocationRelativeTo(null);

        JPanel registerServerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        registerServerPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Register Server")));
        portLabel = new JLabel("Port: ");
        portLabel.setFont(new Font("Serif", Font.BOLD, 20));
        portField = new JTextField(5);
        portField.setFont(new Font("Serif", Font.PLAIN, 15));
        registerServerPanel.add(portLabel);
        registerServerPanel.add(portField);

        JPanel nameServerPanel = new JPanel();
        nameServerPanel.setLayout(new FlowLayout());
        serverNameLabel = new JLabel("Server name: ");
        serverNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        serverNameField = new JTextField(20);
        serverNameField.setFont(new Font("Serif", Font.PLAIN, 15));
        registerServerPanel.add(serverNameLabel);
        registerServerPanel.add(serverNameField);

        registerServerFrame.add(registerServerPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        createServerButton = new JButton("CREATE");
        buttonPanel.add(createServerButton);
        registerServerFrame.add(buttonPanel, BorderLayout.SOUTH);

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

    private void serverFrame() {
        serverFrame.setSize(800, 500);
        serverFrame.setLayout(new BorderLayout());
        serverFrame.setLocationRelativeTo(null);

        JPanel infoServer = new JPanel();
        infoServer.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Server info")));
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
        serverFrame.add(infoServer, BorderLayout.SOUTH);

        selectDirectoryPanel = new JPanel();
        selectDirectoryPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Select directory")));
        selectDirectoryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
        selectDirectory = new JLabel("Select directory: ");
        directoryPath = new JLabel("please select a client!");
        selectDirectoryButton = new JButton("SELECT");
        selectDirectoryPanel.add(selectDirectory);
        selectDirectoryPanel.add(directoryPath);
        selectDirectoryPanel.add(selectDirectoryButton);
        serverFrame.add(selectDirectoryPanel, BorderLayout.NORTH);

        selectDirectoryButton.addActionListener(e -> {
            String selectedClientName = clientList.getSelectedValue();
            if (selectedClientName == null) {
                JOptionPane.showMessageDialog(serverFrame, "Please select a client!");
                return;
            }

            JFrame selectDirectoryFrame = new JFrame("Select directory for " + selectedClientName);
            selectDirectoryFrame.setSize(500, 500);
            selectDirectoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            selectDirectoryFrame.setLayout(new BorderLayout());
            selectDirectoryFrame.setLocationRelativeTo(null);

            ServerHandle serverHandle = ClientList.getClient(selectedClientName);
            JTree tree = serverHandle.getTree();
            JScrollPane treeScrollPanel = new JScrollPane(tree);
            treeScrollPanel.setPreferredSize(new Dimension(300, 300));
            treeScrollPanel.setMaximumSize(treeScrollPanel.getPreferredSize());
            JPanel treePanel = new JPanel(new BorderLayout());

            treePanel.setBorder(new CompoundBorder(new TitledBorder("Directory"), new EmptyBorder(5, 5, 5, 5)));
            treePanel.add(treeScrollPanel, BorderLayout.CENTER);
            selectDirectoryFrame.add(treePanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
            JButton selectButton = new JButton("SELECT");
            
            selectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (selectedNode == null) {
                        JOptionPane.showMessageDialog(selectDirectoryFrame, "Please select a directory!");
                        return;
                    }
                    if (selectedNode.isLeaf()) {
                        JOptionPane.showMessageDialog(selectDirectoryFrame, "Please select a directory!");
                        return;
                    }
                    String path = selectedNode.getUserObject().toString();
                    clientDirectory.put(selectedClientName, path);
                    directoryPath.setText(path);
                    ClientList.getClient(selectedClientName).setPath(path);
                    selectDirectoryFrame.dispose();
                }
            
            });

            buttonPanel.add(selectButton);
            selectDirectoryFrame.add(buttonPanel, BorderLayout.SOUTH);

            selectDirectoryFrame.pack();
            selectDirectoryFrame.setVisible(true);
        });

        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        clientListScrollPanel = new JScrollPane(clientList);
        clientListScrollPanel.setPreferredSize(new Dimension(100,300));
        clientListScrollPanel.setMaximumSize(clientListScrollPanel.getPreferredSize());
        clientListPanel = new JPanel(new BorderLayout());
        clientListPanel.setBorder(new CompoundBorder(new TitledBorder("Client List"), new EmptyBorder(5, 5, 5, 5)));
        clientListPanel.add(clientListScrollPanel, BorderLayout.CENTER);
        serverFrame.add(clientListPanel, BorderLayout.WEST);

        clientList.addListSelectionListener(e->{
            String selectedClientName = clientList.getSelectedValue();
            if (selectedClientName == null) {
                return;
            }
            String path = clientDirectory.get(selectedClientName);
            directoryPath.setText(path);
        });
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(portField.getText()));
            new Thread(){
                @Override
                public void run() {
                    while(true){
                        try {
                            Socket s = ss.accept();
                            ServerHandle serverHandle = new ServerHandle(s);
                            ClientList.addClient(serverHandle);
                            clientListModel.addElement(serverHandle.getClientName());
                            clientDirectory.put(serverHandle.getClientName(), "No directory selected!");
                            serverHandle.start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        msgField.setEditable(false);
        msgField.setFont(new Font("Serif", Font.PLAIN, 20));
        msgScrollPanel = new JScrollPane(msgField);
        msgScrollPanel.setPreferredSize(new Dimension(500, 200));
        msgScrollPanel.setMaximumSize(msgScrollPanel.getPreferredSize());
        msgPanel = new JPanel(new BorderLayout());
        msgPanel.setBorder(new CompoundBorder(new TitledBorder("Message"), new EmptyBorder(5, 5, 5, 5)));
        msgPanel.add(msgScrollPanel, BorderLayout.CENTER);
        serverFrame.add(msgPanel, BorderLayout.CENTER);

        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setVisible(true);

        serverFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ClientList.closeAll();
            }
        } );
    }

    private boolean checkInfo() {
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

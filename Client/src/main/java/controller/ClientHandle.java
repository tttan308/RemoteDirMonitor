package controller;

import model.Client;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Objects;

import static view.MainScreen.*;

public class ClientHandle{
    Client client;
    public static Socket s;
    DataInputStream dis;
    static DataOutputStream dos;
    DefaultMutableTreeNode root;
    DefaultTreeModel treeModel;
    DirectoryMonitor directoryMonitor;

    public ClientHandle(Client client){
        this.client = client;
        try {
            while(true){
                try {
                    s = new Socket(client.getHost(), client.getPort());
                    break;
                } catch (Exception e) {
                    msgRcv.append("Failed to connect to server!").append("\n");
                    msgField.setText(String.valueOf(msgRcv));
                    Thread.sleep(10000);
                }
            }
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(client.getName());
            dos.flush();
            File[] allDirectory = java.io.File.listRoots();
            root = new DefaultMutableTreeNode();
            for (File fileIn : allDirectory) {
                if(fileIn.isDirectory()){
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileIn);
                    File[] files = fileIn.listFiles();
                    if (files != null) {
                        for (File f : files) {
                            DefaultMutableTreeNode nodeChild = new DefaultMutableTreeNode(f);
                            node.add(nodeChild);
                            new Thread(() -> addSubdirectories(nodeChild, f)).start();
                        }
                    }
                    root.add(node);
                }
            }

            treeModel = new DefaultTreeModel(root);
            JTree tree = new JTree(root);
            tree.setShowsRootHandles(true);

            try {
                dos.writeUTF("send tree");
                dos.flush();
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                oos.writeObject(tree);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addSubdirectories(DefaultMutableTreeNode parent, File file) {
        if(!file.isDirectory()) return;
        File[] files = file.listFiles();
        if (files == null) return;
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(childFile.getAbsolutePath());
                parent.add(node);
                addSubdirectories(node, childFile);
            }
        }
    }

    public static void sendMsg(String msg) {
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeSocket(){
        try {
            sendMsg("disconnect");
            sendMsg(client.getName());
            msgRcv.append("\n").append("disconnect");
            msgField.setText(String.valueOf(msgRcv));
            if(directoryMonitor != null) directoryMonitor.interrupt();
            directoryMonitor = null;
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void handle() {
        while(true){
            String rcv;
            try {
                rcv = dis.readUTF();
            } catch (IOException e) {
                break;
            }
            if(Objects.equals(rcv, "disconnect")){
                msgRcv.append("\n").append(rcv);
                msgField.setText(String.valueOf(msgRcv));
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                clientFrame.setVisible(false);
                registerClientFrame.setVisible(true);
                break;
            }
            if(Objects.equals(rcv, "send path")){
                try {
                    String path = dis.readUTF();
                    msgRcv.append("\n").append("Server selected path: ").append(path);
                    msgField.setText(String.valueOf(msgRcv));
                    directoryMonitor = new DirectoryMonitor(Paths.get(path));
                    directoryMonitor.start();
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            msgRcv.append("\n").append(rcv);
            msgField.setText(String.valueOf(msgRcv));
            if(!s.isConnected()) break;
        }
    }
}

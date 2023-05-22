package controller;

import model.ClientList;

import java.io.*;
import java.net.Socket;

import javax.swing.*;

import static view.MainScreen.*;

public class ServerHandle extends Thread{

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final String name;
    private JTree selectedDirectoryTree;
    String path;

    public ServerHandle(Socket s){
        socket = s;
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            name = dis.readUTF();
            msg.append(name).append(" has connected!\n");
            msgField.setText(msg.toString());
            dos.writeUTF("Successfully connected!");
            dos.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getClientName(){
        return name;
    }

    @Override
    public void run() {
        while(true){
            try {
                String msgrcv = dis.readUTF();
                if(msgrcv.equals("disconnect")){
                    String name = dis.readUTF();
                    msg.append(name).append(" has disconnected!\n");
                    msgField.setText(msg.toString());
                    clientDirectory.remove(name);
                    System.out.println(clientDirectory);
                    ClientList.removeClient(name);
                    clientListModel.removeElement(name);
                    directoryPath.setText("");
                    socket.close();
                }

                if(msgrcv.equals("send tree")) {
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    try {
                        selectedDirectoryTree = (JTree) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                msg.append(name).append(": ").append(msgrcv).append("\n");
                msgField.setText(msg.toString());
            } catch (IOException e) {
                break;
            }
        }
    }

    public JTree getTree(){
        return selectedDirectoryTree;
    }

    public void close() {
        try {
            dos.writeUTF("disconnect");
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPath(String path) {
        try {
            dos.writeUTF("send path");
            dos.flush();
            dos.writeUTF(path);
            dos.flush();
            msg.append(name).append(": monitor ").append(path).append("\n");
            msgField.setText(msg.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.path = path;
    }

}

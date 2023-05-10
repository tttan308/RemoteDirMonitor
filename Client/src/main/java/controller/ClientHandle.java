package controller;

import model.Client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientHandle implements Runnable{
    Client client;
    Socket s;
    DataInputStream dis;
    DataOutputStream dos;

    public ClientHandle(Client client){
        this.client = client;
        try {
            s = new Socket(client.getHost(), client.getPort());
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(client.getName());
            System.out.println(client.getName() + " connected");
            dos.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

    }

    public void sendMessage(String message){
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAllDirectory(){
        JTree tree = new JTree();

    }
}

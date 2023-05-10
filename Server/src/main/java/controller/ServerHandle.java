package controller;

import java.io.*;
import java.net.Socket;

public class ServerHandle implements Runnable{

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String name;

    public ServerHandle(Socket s){
        socket = s;
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            name = dis.readUTF();
            System.out.println(name + " connected r");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getName(){
        return name;
    }

    public Socket getSocket(){
        return socket;
    }
    @Override
    public void run() {

    }
}

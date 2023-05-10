package model;

import controller.ClientHandle;

import java.net.Socket;

public class Client implements Runnable{
    private String host;
    private int port;
    private String name;

    public Client(String host, int port, String name){
        this.host = host;
        this.port = port;
        this.name = name;
    }

    public String getHost(){
        return host;
    }

    public int getPort(){
        return port;
    }

    public String getName(){
        return name;
    }
    @Override
    public void run() {
        try {
            Client c = new Client(host, port, name);
            new Thread(new ClientHandle(c)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

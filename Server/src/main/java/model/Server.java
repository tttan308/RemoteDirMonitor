package model;

import controller.ServerHandle;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private int port;
    private ServerSocket serverSocket;

    public Server(int port){
        this.port = port;
    }

    @Override
    public void run() {

    }
}

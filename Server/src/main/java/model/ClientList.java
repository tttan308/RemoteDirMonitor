package model;

import controller.ServerHandle;

import java.net.Socket;
import java.util.ArrayList;

public class ClientList {
    public static ArrayList<Socket> clients = new ArrayList<>();
    public static ArrayList<String> clientNames = new ArrayList<>();

    public static void addClient(ServerHandle client) {
        clients.add(client.getSocket());
        clientNames.add(client.getName());
    }

    public static void clear(){
        clients.clear();
        clientNames.clear();
    }
}

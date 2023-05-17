package model;

import controller.ServerHandle;

import java.util.HashMap;
import java.util.Map;


public class ClientList{
    public static Map<String, ServerHandle> clients = new HashMap<>();
    public static void addClient(ServerHandle client) {
        clients.put(client.getClientName(), client);
    }

    public static void removeClient(String name) {
        clients.remove(name);
    }


    public static ServerHandle getClient(String clientName) {
        return clients.get(clientName);
    }

    public static void closeAll() {
        for (ServerHandle client : clients.values()) {
            try {
                client.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

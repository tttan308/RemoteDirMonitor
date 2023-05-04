package src.model;

public class Server {
    private String serverName;
    private int port;
    private String serverIP;

    public Server(String serverName, int port, String serverIP) {
        this.serverName = serverName;
        this.port = port;
        this.serverIP = serverIP;
    }

    public String getServerName() {
        return serverName;
    }

    public int getPort() {
        return port;
    }

    public String getServerIP() {
        return serverIP;
    }
}

package SoftEng_2024.Network.ToModel;


import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class SocketServer{
    private final int port;
    private ServerSocket serverSocket;
    private final NetworkManager manager;
    private ConcurrentHashMap<Double, Socket> clientsConnected = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Double, ObjectOutputStream> clientsOut = new ConcurrentHashMap<>();

    public SocketServer(int port, NetworkManager manager) {
        this.port = port;
        this.manager = manager;
    }


    public void startServer(SocketServer server) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("SERVER STARTED AT PORT:" + port);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Thread t = new SocketClientHandler(server, socket, manager);
                    t.start();
                } catch (IOException e) {
                    System.err.println("Error accepting client connection... ");
                    break;
                }
            }
        }catch (IOException e){
            System.err.println("Error starting server... ");
        } finally {
           serverSocket.close();
        }
    }

    public void setClientsConnected(double id, Socket socket) {
        this.clientsConnected.put(id, socket);
    }

    public void setClientsOut(double id, ObjectOutputStream out) {
        this.clientsOut.put(id,out);
    }

    public ConcurrentHashMap<Double, Socket> getClientsConnected() {
        return clientsConnected;
    }

    public ConcurrentHashMap<Double, ObjectOutputStream> getClientsOut() {
        return clientsOut;
    }

    public void unRegisterClient(double id) {
        if (clientsConnected.containsKey(id)) {
            clientsConnected.remove(id);
        }else {
            System.err.println("ID NOT REGISTERED...");
        }
        if (clientsOut.containsKey(id)) {
            clientsOut.remove(id);
        }else{
            System.err.println("ID NOT REGISTERED...");
        }
    }
}
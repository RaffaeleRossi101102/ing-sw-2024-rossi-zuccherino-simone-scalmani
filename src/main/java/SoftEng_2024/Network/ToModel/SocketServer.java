package SoftEng_2024.Network.ToModel;


import SoftEng_2024.Model.ModelMessages.ModelMessage;

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
            System.out.println("Socket Server started on port " + port);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Thread t = new SocketClientHandler(server, socket, manager);
                    t.start();
                }catch (IOException e) {
                    System.err.println("Error accepting client connection... ");
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

    public ServerSocket getServerSocket() {
        return serverSocket;
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

    public void unRegisterClient(double id) throws IOException {
        if (clientsOut.containsKey(id)) {
            ObjectOutputStream out = clientsOut.remove(id);
            out.close();
        }else{
            System.err.println("ID not registered...");
        }
        if (clientsConnected.containsKey(id)) {
            Socket remove = clientsConnected.remove(id);
            remove.close();
        }else {
            System.err.println("ID not registered...");
        }
    }

    //QUANDO RICEVE UN MODELMSG LO INVIA NEL SOCKET VERSO IL CLIENT CHE ASCOLTA
    public void addToClientQueue(ModelMessage msg) {
        if(msg.getReceiverID() == 0.0){
            for(double id : clientsOut.keySet()){
                ObjectOutputStream out = clientsOut.get(id);
                try {
                    out.writeObject(msg);
                    out.flush();
                    out.reset();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            for (double ID : clientsOut.keySet()) {
                try {
                    //se l'ID contenuto nel messaggio è lo stesso di uno dei client collegati con
                    //socket, lo manda. Atrimenti significa che il client di destinazione è di tipo RMI
                    if (ID == msg.getReceiverID()) {
                        ObjectOutputStream out = clientsOut.get(ID);
                        out.writeObject(msg);
                        out.flush();
                        out.reset();
                    }
                } catch (IOException e) {
                    //TODO: lancia l'eccezione o no?
                    System.out.println("ERROR WRITING OBJECT...");
                }
            }
        }
    }
}
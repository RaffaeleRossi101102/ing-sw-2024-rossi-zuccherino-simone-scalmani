package SoftEng_2024.Network.ToModel;


import SoftEng_2024.Model.ModelMessages.ModelMessage;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a server that listens for incoming client connections
 * via sockets and manages communication with connected clients.
 */
public class SocketServer{
    private final int port;
    private ServerSocket serverSocket;
    private final NetworkManager manager;
    private ConcurrentHashMap<Double, Socket> clientsConnected = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Double, ObjectOutputStream> clientsOut = new ConcurrentHashMap<>();

    /**
     * Constructor to initialize a SocketServer object.
     *
     * @param port    The port number on which the server listens for incoming connections.
     * @param manager The NetworkManager instance for managing network operations.
     */
    public SocketServer(int port, NetworkManager manager) {
        this.port = port;
        this.manager = manager;
    }

    /**
     * Starts the server on the specified port, accepting incoming client connections
     * and spawning a new SocketClientHandler thread for each client connection.
     *
     * @param server The SocketServer instance to start.
     * @throws IOException If an I/O error occurs while starting the server.
     */
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
                    System.out.println("Error accepting client connection... ");
                }
            }
        }catch (IOException e){
            System.out.println("[ERROR] Error starting server... ");
        } finally {
           serverSocket.close();
        }
    }

    /**
     * Sets a client's socket connection in the map of connected clients.
     *
     * @param id     The client's ID.
     * @param socket The socket connection associated with the client.
     */
    public void setClientsConnected(double id, Socket socket) {
        this.clientsConnected.put(id, socket);
    }

    /**
     * Retrieves the ServerSocket instance of this server.
     *
     * @return The ServerSocket instance.
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * Sets a client's ObjectOutputStream in the map of output streams for connected clients.
     *
     * @param id  The client's ID.
     * @param out The ObjectOutputStream associated with the client.
     */
    public void setClientsOut(double id, ObjectOutputStream out) {
        this.clientsOut.put(id,out);
    }
    /**
     * Retrieves the map of connected clients and their associated sockets.
     *
     * @return The ConcurrentHashMap of client IDs mapped to their sockets.
     */
    public ConcurrentHashMap<Double, Socket> getClientsConnected() {
        return clientsConnected;
    }
    /**
     * Retrieves the map of connected clients and their associated ObjectOutputStreams.
     *
     * @return The ConcurrentHashMap of client IDs mapped to their ObjectOutputStreams.
     */
    public ConcurrentHashMap<Double, ObjectOutputStream> getClientsOut() {
        return clientsOut;
    }
    /**
     * Unregisters a client by closing its ObjectOutputStream and Socket.
     *
     * @param id The ID of the client to unregister.
     * @throws IOException If an I/O error occurs while closing the streams or socket.
     */
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
    /**
     * Adds a ModelMessage to the queue for all connected clients or a specific client.
     *
     * @param msg The ModelMessage to send to clients.
     */
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
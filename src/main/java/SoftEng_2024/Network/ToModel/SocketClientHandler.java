package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.ViewMessages.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public class SocketClientHandler extends Thread  {
    private Socket socket;
    private NetworkManager manager;
    private ViewMessage message;
    private double id;
    private ConcurrentHashMap<Double, Socket> clientsConnected = new ConcurrentHashMap<>();

    public SocketClientHandler(Socket socket, NetworkManager manager) {
        this.socket = socket;
        this.manager = manager;
    }
    public void addToNetworkManager(ViewMessage msg) throws RemoteException {

    }
    public void run() {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        DataInputStream dis = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            //RECEIVING THE CLIENT-ID
            id = dis.readDouble();
            clientsConnected.put(id, socket);
            while(true) {
                //RECIVING MESSAGES FROM CLIENT AND ADDING THEM TO THE NETWORKMANAGER QUEUE
                message = (ViewMessage) in.readObject();
                addToQueue(message);
                System.out.println("MESSAGE ADDED TO QUEUE");
            }
        } catch (IOException | ClassNotFoundException ignored){
        }
    }
    public void registerClient(double ID, ClientInterface client)  {
    }


    public void unregisterClient(double ID) {
        if (clientsConnected.containsKey(ID)) {
            try {
                clientsConnected.remove(ID).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
            System.err.println("Removing client from RMI server");
    }


    public void addToClientQueue(ModelMessage msg) throws IOException {
        for (double id : clientsConnected.keySet()) {
            Socket sok = clientsConnected.get(id);
            ObjectOutputStream out = new ObjectOutputStream(sok.getOutputStream());
            out.writeObject(msg);
            out.flush();
        }
    }

    private void addToQueue(ViewMessage msg) {
        manager.addViewMessages(msg);
    }

}


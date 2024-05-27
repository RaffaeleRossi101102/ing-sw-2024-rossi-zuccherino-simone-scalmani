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

public class SocketClientHandler extends Thread {
    private Socket socket;
    private NetworkManager manager;
    private ViewMessage message;
    private double id;
    private SocketServer server;

    public SocketClientHandler(SocketServer server, Socket socket, NetworkManager manager) {
        this.server = server;
        this.socket = socket;
        this.manager = manager;
    }


    public void run(){
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        DataInputStream dis = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            //RECEIVING THE CLIENT-ID
            id = dis.readDouble();
            server.setClientsConnected(id, this.socket);
            server.setClientsOut(id, out);
            while(true) {
                //RECIVING MESSAGES FROM CLIENT AND ADDING THEM TO THE NETWORKMANAGER QUEUE
                message = (ViewMessage) in.readObject();
                addToQueue(message);
                System.out.println("MESSAGE ADDED TO QUEUE");
            }
        } catch (IOException e) {
            System.out.println("IO EXCEPTION...");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR READING OBJECT...");
        }finally {
            try {
                in.close();
                out.close();
                dis.close();
            } catch (IOException e) {
                System.out.println("ERROR CLOSING OUTPUT/INPUT STREAM...");
            }
        }
    }

    //QUANDO RICEVE UN MODELMSG LO INVIA NEL SOCKET VERSO IL CLIENT CHE ASCOLTA
    public void addToClientQueue(ModelMessage msg) throws IOException {
        for(ObjectOutputStream out : server.getClientsOut().values()) {
            try {
                out.writeObject(msg);
                out.flush();
                out.reset();
            } catch (IOException e) {
                System.out.println("ERROR WRITING OBJECT...");
            }
        }
    }

    private void addToQueue(ViewMessage msg) {
        manager.addViewMessages(msg);
    }

}


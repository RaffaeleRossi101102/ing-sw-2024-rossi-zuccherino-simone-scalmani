package SoftEng_2024.Network.ToModel;

import SoftEng_2024.View.ViewMessages.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;

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
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            //RECEIVING THE CLIENT-ID
            id = in.readDouble();
            System.out.println("ID: " + id);
            server.setClientsConnected(id, this.socket);
            server.setClientsOut(id, out);
            while(true) {
                try {
                    message = (ViewMessage) in.readObject();
                }catch (SocketTimeoutException e) {
                    System.out.println("CLIENT CRASHED...(because i dont received message)");
                }
                if (message.getClass() != Pong.class) {
                    addToQueue(message);
                    System.out.println("MESSAGE ADDED TO QUEUE");
                }else{
                    System.out.println("PONG");
                }
            }
        } catch (IOException e) {
            System.out.println("IO EXCEPTION...");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR READING OBJECT...");
        }finally {
            try {
                Objects.requireNonNull(in).close();
                Objects.requireNonNull(out).close();
            } catch (IOException e) {
                System.out.println("ERROR CLOSING OUTPUT/INPUT STREAM...");
            }
        }
    }

    private void addToQueue(ViewMessage msg) {
        manager.addViewMessages(msg);
    }

}


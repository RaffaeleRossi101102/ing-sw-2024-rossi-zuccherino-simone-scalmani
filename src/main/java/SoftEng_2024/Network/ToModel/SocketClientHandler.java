package SoftEng_2024.Network.ToModel;

import SoftEng_2024.View.ViewMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class SocketClientHandler extends Thread{
    private Socket socket;
    private NetworkManager manager;
    private ViewMessage message;
    public SocketClientHandler (Socket socket, NetworkManager manager) {
        this.socket = socket;
        this.manager = manager;
    }
    public void run() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            while(true) {
                message = (ViewMessage) in.readObject();
                addToQueue(message);
                System.out.println("MESSAGE ADDED TO QUEUE");
            }
        } catch (IOException | ClassNotFoundException ignored){
        }
    }
    private void addToQueue(ViewMessage msg) {
        manager.addViewMessages(msg);
    }
}
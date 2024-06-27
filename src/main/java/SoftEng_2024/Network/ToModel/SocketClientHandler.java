package SoftEng_2024.Network.ToModel;

import SoftEng_2024.View.ViewMessages.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class handles communication with a client connected via a socket.
 * It receives messages from the client and manages message processing.
 */
public class SocketClientHandler extends Thread {
    private Socket socket;
    private NetworkManager manager;
    private ViewMessage message;
    private double id;
    private SocketServer server;
    private boolean received;
    private boolean firstTime;

    /**
     * Constructor to initialize a SocketClientHandler object.
     *
     * @param server   The SocketServer instance managing this client connection.
     * @param socket   The Socket connected to the client.
     * @param manager  The NetworkManager instance for managing network operations.
     */
    public SocketClientHandler(SocketServer server, Socket socket, NetworkManager manager) {
        this.server = server;
        this.socket = socket;
        this.manager = manager;
        this.received = false;
        this.firstTime = true;
    }

    /**
     * Overrides the run method of Thread.
     * Handles the communication loop with the client, receiving messages and managing timeouts.
     */
    public void run(){
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            //RECEIVING THE CLIENT-ID
            id = in.readDouble();
            server.setClientsConnected(id, this.socket);
            server.setClientsOut(id, out);
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {

                @Override
                public void run() {
                        System.out.println("CLIENT CRASHED...");
                        addToQueue(new QuitMessage(id));
                }
            };
            timer.schedule(timerTask,1000, 5000);
            while(true) {
                message = (ViewMessage) in.readObject();
                timer.cancel();
                if (message.getClass() != Pong.class) {
                    System.out.println(message);
                    addToQueue(message);
                    System.out.println("MESSAGE ADDED TO QUEUE");
                }
            }
        } catch (IOException e) {
            System.out.println("IO EXCEPTION...");
            addToQueue(new QuitMessage(id));
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR READING OBJECT...");
            addToQueue(new QuitMessage(id));
        }finally {
            try {
                Objects.requireNonNull(in).close();
                Objects.requireNonNull(out).close();
            } catch (IOException e) {
                System.out.println("ERROR CLOSING OUTPUT/INPUT STREAM...");
            }
        }
    }

    /**
     * Adds a ViewMessage to the NetworkManager's queue for processing.
     *
     * @param msg The ViewMessage to add to the queue.
     */
    private void addToQueue(ViewMessage msg) {
        manager.addViewMessages(msg);
    }

}


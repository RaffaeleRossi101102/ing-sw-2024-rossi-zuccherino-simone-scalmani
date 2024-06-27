package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.MainView;
import SoftEng_2024.View.View;
import SoftEng_2024.View.ViewMessages.*;



import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * SocketClient class implements the ClientInterface for communication with a server over sockets.
 */
public class SocketClient implements ClientInterface {
    private View view;
    private String ip;
    private int port;
    private Socket socket;
    private  double ID;
    private Pong pong;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean socketCreated;
    private LinkedBlockingQueue<ModelMessage> modelQueue;
    private boolean clientRunning;

    /**
     * Constructs a SocketClient object.
     *
     * @param ip   The IP address of the server.
     * @param port The port number for socket connection.
     * @param ID   The unique identifier for the client.
     */
    public SocketClient(String ip, int port, double ID){
        this.ip =ip;
        this.port = port;
        this.ID = ID;
        socketCreated = false;
        modelQueue = new LinkedBlockingQueue<>();
        pong = new Pong(ID, 1.0);
        clientRunning = true;
    }

    /**
     * Starts the client by establishing a connection to the server and initializing streams.
     */
    public void startClient(){
        try {
            clientRunning = true;
            //STARTING CONNECTION
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeDouble(ID);
            out.flush();
            //THREAD CHE STA IN ASCOLTO DEI MESSAGGI CHE ARRIVANO DAL SERVER
            Thread t = new Thread(() -> {
                try {
                    in = new ObjectInputStream(socket.getInputStream());
                    while(true){
                        try {
                            ModelMessage message= (ModelMessage) in.readObject();
                            addToViewQueue(message);
                        } catch (ClassNotFoundException | IOException e) {
                            //System.out.println("You're going to be disconnected, the game has started");
                            clientRunning = false;
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("ERROR INITIALIZING INPUT STREAM...");
                }
            });
            t.start();
            pong();
        }catch (IOException e) {
            System.out.println("ERROR CONNECTING CLIENT TO SERVER...");
        }
    }

    /**
     * Sends an update message to the server.
     *
     * @param msg The update message to be sent.
     */
    public synchronized void update(ViewMessage msg) {
        try {
            out.writeObject(msg);
            out.flush();
            out.reset();
        } catch (IOException e) {
            System.out.println("[ERROR] Something went terribly wrong, shutting down");
            System.exit(0);
        }
    }

    //INVIA IL MESSAGGIO DI QUIT E POI CHIUDE LA CONNESSIONE
    /**
     * Sends a quit message to the server and closes the connection.
     *
     * @param msg The quit message to be sent.
     * @throws IOException If there is an I/O related exception.
     */
    @Override
    public void quit(ViewMessage msg) throws IOException {
        update(msg);
        //DEVO CHIUDERE IL THREAD???
        try {
            this.out.close();
            this.in.close();
            this.socket.close();
        }catch (IOException e){
            System.out.println("ERROR CLOSING OUTPUT/INPUT STREAM OR SOCKET...");
        }
    }

    //METODO CHE RICEVE I MESSAGI DAL SERVER E LI AGGIUNGE ALLA QUEUE DEL CLIENT
    /**
     * Adds a model message to the client's view queue.
     *
     * @param msg The model message to add to the queue.
     * @throws RemoteException If there is an RMI related exception.
     */
    @Override
    public synchronized void addToViewQueue(ModelMessage msg) throws RemoteException {
        this.modelQueue.add(msg);
        notifyAll();
    }
    /**
     * Runs the client, continuously processing messages from the server.
     *
     * @throws RemoteException    If there is an RMI related exception.
     * @throws InterruptedException If the thread is interrupted.
     */
    @Override
    public void run() throws RemoteException, InterruptedException {
        while (true) {
            pollThreaded();
        }
    }

    //CREA IL SOCKET E AGGIUNGE L'ID NELLA MAPPA DEL SERVER
    /**
     * Registers the client to the server.
     *
     * @param ID     The unique identifier for the client.
     * @param client The client interface instance.
     * @throws RemoteException If there is an RMI related exception.
     */
    public void registerToServer(double ID, ClientInterface client) throws RemoteException{

        //if(!socketCreated){
        startClient();
        //socketCreated = true;
        //}
    }

    //METODO CHE VIENE CHIAMATO DALLA VIEW PER DARE IL RIFERIMENTO AL CLIENT
    /**
     * Sets the view associated with this client.
     *
     * @param view The view instance.
     * @throws RemoteException If there is an RMI related exception.
     */
    @Override
    public void setView(View view) throws RemoteException {
        this.view = view;
    }

    /**
     * Sends periodic 'pong' messages to the server to maintain connectivity.
     *
     * @throws RemoteException If there is an RMI related exception.
     */
    @Override
    public void pong() throws RemoteException {
        Thread t1 = new Thread(() -> {
            while(clientRunning){
                update(pong);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
    }


    /**
     * Processes model messages from the queue in a separate thread.
     *
     * @throws RemoteException    If there is an RMI related exception.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    private synchronized void pollThreaded() throws RemoteException, InterruptedException {
        if (modelQueue.isEmpty()) {
            wait();
        } else {
            Thread t = new Thread(() -> {
                try {
                    this.modelQueue.take().executeMessage(this.view);
                } catch (InterruptedException e) {
                    System.out.println("[ERROR] SOMETHING WENT WRONG WHILE EXECUTING THE MESSAGE");

                }
            });
            t.start();
        }
    }
}
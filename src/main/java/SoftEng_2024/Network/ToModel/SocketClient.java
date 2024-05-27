package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.View;
import SoftEng_2024.View.ViewMessages.*;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;


public class SocketClient implements ClientInterface {
    private View view;
    private String ip;
    private int port;
    private Socket socket;
    private  double ID;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private DataOutputStream dos;
    private boolean socketCreated;
    private LinkedBlockingQueue<ModelMessage> modelQueue;

    public SocketClient(String ip, int port, double ID){
        this.ip =ip;
        this.port = port;
        this.ID = ID;
        socketCreated = false;
        modelQueue = new LinkedBlockingQueue<>();
    }


    public void startClient(){
        try {
            //STARTING CONNECTION
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeDouble(ID);
            //THREAD CHE STA IN ASCOLTO DEI MESSAGGI CHE ARRIVANO DAL SERVER
            Thread t = new Thread(() -> {
                while(true){
                    try {
                        ModelMessage message= (ModelMessage) in.readObject();
                        addToViewQueue(message);
                    } catch (ClassNotFoundException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            t.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(ViewMessage msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException ignored) {
        }
    }

    //INVIA IL MESSAGGIO DI QUIT E POI CHIUDE LA CONNESSIONE
    @Override
    public void quit(ViewMessage msg) throws IOException {
        update(msg);
        this.socket.close();
    }

    //METODO CHE RICEVE I MESSAGI DAL SERVER E LI AGGIUNGE ALLA QUEUE DEL CLIENT
    @Override
    public void addToViewQueue(ModelMessage msg) throws RemoteException {
        this.modelQueue.add(msg);
    }
    @Override
    public void run() throws RemoteException{
        while(true){
            pollThreaded();
        }
    }
    //CREA IL SOCKET E AGGIUNGE L'ID NELLA MAPPA DEL SERVER
    public void registerToServer(double ID, ClientInterface client) throws RemoteException{
        if(!socketCreated){
            startClient();
            socketCreated = true;
        }
    }

    //METODO CHE VIENE CHIAMATO DALLA VIEW PER DARE IL RIFEREMENTO AL CLIENT
    @Override
    public void setView(View view) throws RemoteException {
        this.view = view;
    }

    private void pollThreaded() throws RemoteException {
        Thread t = new Thread(() -> {
            try {
                this.modelQueue.take().executeMessage(this.view);
            } catch (InterruptedException e) {
                System.err.println("Something went wrong while executing the messages");
                throw new RuntimeException(e);
            }

        });
        t.start();
    }


}
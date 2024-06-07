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
    private Pong pong;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean socketCreated;
    private LinkedBlockingQueue<ModelMessage> modelQueue;

    public SocketClient(String ip, int port, double ID){
        this.ip =ip;
        this.port = port;
        this.ID = ID;
        socketCreated = false;
        modelQueue = new LinkedBlockingQueue<>();
        pong = new Pong(ID, 1.0);
    }


    public void startClient(){
        try {
            //STARTING CONNECTION
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeDouble(ID);
            //THREAD CHE STA IN ASCOLTO DEI MESSAGGI CHE ARRIVANO DAL SERVER
            Thread t = new Thread(() -> {
                    while (true) {
                        try {
                            ModelMessage message = (ModelMessage) in.readObject();
                            addToViewQueue(message);
                        } catch (ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
            });
            t.start();
            pong();
        } catch (IOException e) {
            System.out.println("ERROR CONNECTING CLIENT TO SERVER...");
        }
    }


    public void update(ViewMessage msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException ignored) {
            System.out.println("ERROR WRITING OBJECT 2...");
        }
    }

    //INVIA IL MESSAGGIO DI QUIT E POI CHIUDE LA CONNESSIONE
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
    @Override
    public void addToViewQueue(ModelMessage msg) throws RemoteException {
        this.modelQueue.add(msg);
    }
    @Override
    public void run() throws RemoteException{
        startClient();
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

    //METODO CHE VIENE CHIAMATO DALLA VIEW PER DARE IL RIFERIMENTO AL CLIENT
    @Override
    public void setView(View view) throws RemoteException {
        this.view = view;
    }

    @Override
    public void pong() throws RemoteException {
        Thread t1 = new Thread(() -> {
            while(true){
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

    private void pollThreaded() throws RemoteException {
        Thread t = new Thread(() -> {
            try {
                this.modelQueue.take().executeMessage(this.view);
            } catch (InterruptedException e) {
                System.err.println("SOMETHING WENT WRONG WHILE EXECUTING MESSAGE");

            }

        });
        t.start();
    }
}
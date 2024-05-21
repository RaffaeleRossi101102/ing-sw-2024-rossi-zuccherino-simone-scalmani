package SoftEng_2024.Network.ToModel;


import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.io.*;
import java.net.*;
import java.rmi.*;

public class SocketServer{
    private final int port;
    private ServerSocket serverSocket;
    private final NetworkManager manager;
    public SocketServer(int port, NetworkManager manager) {
        this.port = port;
        this.manager = manager;
    }
    public void startServer() throws IOException {
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("SERVER STARTED AT PORT:" + port);
        } catch(IOException e){
            System.err.println(e.getMessage());
            return;
        }
        while(true){
            try{
                Socket socket = serverSocket.accept();
                Thread t = new SocketClientHandler(socket, manager);
                t.start();
            }catch(IOException e){
                break; //esce quando si chiude il server
            }
        }
    }




    public void addToNetworkManager(ViewMessage msg)  {

    }


    public void run() {

    }


    public void registerClient(double ID)  {
        //TODO il serverSocket avrà una mappa in cui la chiave sono gli ID dei client e il valore sono i socket.
        //TODO Aggiungi alla mappa il socket e l'ID
    }

    public void unregisterClient(double ID) {
        //TODO elimina dalla mappa il socket da non considerare per l'invio dei messaggi
    }

    public void addToClientQueue(ModelMessage msg){

    }
}
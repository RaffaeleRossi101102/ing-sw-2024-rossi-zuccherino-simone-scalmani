package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.View;
import SoftEng_2024.View.ViewMessages.*;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;


public class SocketClient implements ClientInterface {

    private String ip;
    private int port;
    Socket socket;
    ObjectOutputStream out;

    public SocketClient(String ip, int port){
        this.ip =ip;
        this.port = port;
    }


    public void startClient(){
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());

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

    @Override
    public void quit(ViewMessage msg) throws RemoteException {

    }

    @Override
    public void addToViewQueue(ModelMessage msg) throws RemoteException {

    }

    @Override
    public void run() throws RemoteException {

    }

    public void registerToServer(double ID, ClientInterface client) throws RemoteException{

    }

    @Override
    public void setView(View view) throws RemoteException {

    }
}
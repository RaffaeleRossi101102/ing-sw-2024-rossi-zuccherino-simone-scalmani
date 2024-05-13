package SoftEng_2024.Network.ToModel;

import SoftEng_2024.View.Messages.MessageView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI extends UnicastRemoteObject implements ClientInterface {
    ServerInterface server;
    double ID;

    public ClientRMI(ServerInterface server, double ID) throws RemoteException {
        this.server = server;
        this.ID = ID;
    }


    @Override
    public void update(MessageView msg) throws RemoteException {
        server.addToQueue(msg);
    }

    @Override
    public void quit(MessageView msg) throws RemoteException {

    }


}

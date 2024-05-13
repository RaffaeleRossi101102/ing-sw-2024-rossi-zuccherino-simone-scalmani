package SoftEng_2024.Network.ToModel;

import SoftEng_2024.View.Messages.MessageView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI extends UnicastRemoteObject implements ClientInterface {
    ServerInterface server;

    public ClientRMI(ServerInterface server) throws RemoteException {
        this.server = server;
    }


    @Override
    public void update(MessageView msg) throws RemoteException {
        server.addToQueue(msg);
    }





}

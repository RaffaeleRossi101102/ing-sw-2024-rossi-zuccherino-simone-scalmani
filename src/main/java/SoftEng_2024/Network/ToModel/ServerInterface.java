package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void addToNetworkManager(ViewMessage msg) throws RemoteException;

    void run() throws RemoteException, AlreadyBoundException;
    void registerClient(double ID, ClientInterface client) throws RemoteException;
    void unregisterClient(double ID) throws RemoteException;
    void addToClientQueue(ModelMessage msg) throws RemoteException;
}

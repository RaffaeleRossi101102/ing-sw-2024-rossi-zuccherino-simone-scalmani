package SoftEng_2024.Network.ToModel;

import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void update(ViewMessage msg) throws RemoteException;
    void quit(ViewMessage msg) throws RemoteException;
}

package SoftEng_2024.Network;

import SoftEng_2024.View.MessageView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void update(MessageView msg) throws RemoteException;
}

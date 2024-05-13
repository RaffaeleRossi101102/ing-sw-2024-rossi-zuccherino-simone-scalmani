package SoftEng_2024.Network.ToModel;

import SoftEng_2024.View.Messages.MessageView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void update(MessageView msg) throws RemoteException;
    void quit(MessageView msg) throws RemoteException;
}

package SoftEng_2024.Network.ToModel;

import SoftEng_2024.View.Messages.MessageView;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void addToQueue(MessageView msg) throws RemoteException;
    void run() throws RemoteException, AlreadyBoundException;
}

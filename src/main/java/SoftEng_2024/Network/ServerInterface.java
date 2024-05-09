package SoftEng_2024.Network;

import SoftEng_2024.View.MessageView;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void addToQueue(MessageView msg) throws RemoteException;
    void run() throws RemoteException, AlreadyBoundException;
}

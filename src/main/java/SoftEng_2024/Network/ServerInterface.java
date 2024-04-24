package SoftEng_2024.Network;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void connect(ClientInterface client) throws RemoteException;
    public void startGame() throws RemoteException;
    void notifyAllClients(String s)throws RemoteException;
}

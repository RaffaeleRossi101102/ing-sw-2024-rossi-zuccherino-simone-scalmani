package SoftEng_2024.Network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void showServerMessage(String message) throws RemoteException;
    int choosePlayersMax() throws RemoteException;
    void showServerError(String error) throws RemoteException;
    String setNickname() throws RemoteException;
    boolean heartBeat() throws RemoteException;
}

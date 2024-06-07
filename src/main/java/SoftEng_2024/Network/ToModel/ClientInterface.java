package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.View;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void update(ViewMessage msg) throws RemoteException;
    void quit(ViewMessage msg) throws IOException;
    void addToViewQueue(ModelMessage msg) throws RemoteException;
    void run() throws RemoteException, InterruptedException;
    void registerToServer(double ID, ClientInterface client) throws RemoteException, NotBoundException;
    void setView(View view) throws RemoteException;
    void pong() throws RemoteException;
}

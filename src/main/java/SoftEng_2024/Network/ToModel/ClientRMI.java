package SoftEng_2024.Network.ToModel;

import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI extends UnicastRemoteObject implements ClientInterface {
    ServerInterface server;
    double ID;

    public ClientRMI(double ID) throws RemoteException {
        this.modelQueue=new LinkedBlockingQueue<>();
        this.ID = ID;
    }

    public void run(){
        while(true){
            pollThreaded();
        }
    }
    @Override
    public void update(ViewMessage msg) throws RemoteException {
        server.addToNetworkManager(msg);
    }

    @Override
    public void quit(ViewMessage msg) throws RemoteException {

    }


}

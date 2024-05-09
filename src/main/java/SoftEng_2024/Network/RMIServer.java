package SoftEng_2024.Network;

import SoftEng_2024.View.MessageView;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer implements ServerInterface{
    private NetworkManager manager;
    //**************************************
    //METHODS
    //CONSTRUCTOR
    public RMIServer(NetworkManager manager) throws RemoteException{
        this.manager = manager;
    }
    @Override
    public void addToQueue(MessageView msg) throws RemoteException {
        manager.addViewMessages(msg);
    }
    public void run() throws RemoteException, AlreadyBoundException {
        String registryName= "localhost";
        ServerInterface stub= (ServerInterface) UnicastRemoteObject.exportObject(this,0);
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(6969);
        } catch (RemoteException e) {
            throw new RuntimeException("Something went wrong, retry... ");
        }
        try {
            registry.bind(registryName, stub);
            System.out.println("ServerBound");
        } catch (RemoteException remoteException ) {
            throw new RuntimeException("Something went wrong, retry... ");
        }
    }
}

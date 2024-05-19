package SoftEng_2024.Network.ToView;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ServerInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIObServer implements Remote {
    private List<ClientInterface> clients;

    public void run() throws RemoteException {
        String registryName = "localhost";
        RMIObServer stub = (RMIObServer) UnicastRemoteObject.exportObject(this,0);
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(9696);
        } catch (RemoteException e) {
            throw new RuntimeException("Something went wrong, retry... ");
        }
        try {
            registry.bind(registryName, stub);
            System.out.println("ToViewServerBound");
        } catch (RemoteException | AlreadyBoundException remoteException ) {
            throw new RuntimeException("Something went wrong, retry... ");
        }
    }
}

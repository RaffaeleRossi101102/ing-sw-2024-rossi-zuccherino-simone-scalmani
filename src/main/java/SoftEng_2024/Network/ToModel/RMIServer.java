package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.ViewMessages.QuitMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class RMIServer implements ServerInterface{
    private final NetworkManager manager;
    private ConcurrentHashMap<Double, ClientInterface> IdClientBindingMap;
    private ModelMessage msg;

    //**************************************
    //METHODS
    //CONSTRUCTOR
    public RMIServer(NetworkManager manager) throws RemoteException{
        this.manager = manager;
        IdClientBindingMap = new ConcurrentHashMap<>();
    }
    @Override
    public void addToNetworkManager(ViewMessage msg) throws RemoteException {
        manager.addViewMessages(msg);
    }
    public void run() throws RemoteException, AlreadyBoundException {
        String registryName = "localhost";
        ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this,0);
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(6969);
        } catch (RemoteException e) {
            throw new RuntimeException("Something went wrong, retry... ");
        }
        try {
            registry.bind(registryName, stub);
            System.out.println("ToModelServerBound");
        } catch (RemoteException remoteException ) {
            throw new RuntimeException("Something went wrong, retry... ");
        }

    }

    //registers the client to the hash map
    @Override
    public void registerClient(double ID, ClientInterface client) throws RemoteException{

        if (!IdClientBindingMap.containsKey(ID)) {
            IdClientBindingMap.put(ID, client);
        }else {
            System.err.println("ID already mapped...");
        }
    }
    //removes the client from the hash map
    @Override
    public void unregisterClient(double ID) throws RemoteException{

        if (IdClientBindingMap.containsKey(ID)) {
            IdClientBindingMap.remove(ID);
        }else {
            System.err.println("ID not found in RMI hashmap...");
        }
    }

    @Override
    public void addToClientQueue(ModelMessage msg) throws RemoteException {
        //for each client that is connected
        for(double ID: IdClientBindingMap.keySet()){
            try{
                //adds the message to the client's modelMessageQueue
                IdClientBindingMap.get(ID).addToViewQueue(msg);
            }catch(RemoteException re){

                //client probably crashed or something went wrong with his stub
                unregisterClient(ID);
                ViewMessage message = new QuitMessage(ID);
                manager.addViewMessages(message);
            }
        }
    }

}

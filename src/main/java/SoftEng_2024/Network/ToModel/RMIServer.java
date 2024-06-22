package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.ViewMessages.QuitMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;

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

        Thread pingThread = new Thread(() -> {
            while (true) {
                if (!IdClientBindingMap.isEmpty()){
                    ping();
                }

                try {
                    //noinspection BusyWait
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        });
        pingThread.start();
    }

    private void ping() {
        for(Double id : IdClientBindingMap.keySet()){
            try {
                IdClientBindingMap.get(id).pong();
            }catch (RemoteException e){
                System.err.println("Client with id: " + id + " has crashed or disconnected, sending quit message...");
                ViewMessage msg = new QuitMessage(id);
                try {
                    addToNetworkManager(msg);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
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
        IdClientBindingMap.remove(ID);
    }

    @Override
    public void addToClientQueue(ModelMessage msg) throws RemoteException {
       // System.out.println("trying to send "+msg);
        try {
            //if the ID==0 it means that the messge has to be sent to everyone
            if (msg.getReceiverID() == 0) {
                for (ClientInterface client : IdClientBindingMap.values()) {
                    client.addToViewQueue(msg);
                }
            }
            else{
                //for each ID registered in the map
                for(double ID: IdClientBindingMap.keySet()){
                    //if the message is destined to a client connected to the RMI server, send it
                    if(msg.getReceiverID()==ID) {
                        IdClientBindingMap.get(ID).addToViewQueue(msg);
                        //System.out.println("Mandato oggetto "+msg);
                    }
                }
            }
        }catch(RemoteException re){
            //TODO: quit message?
        //client probably crashed or something went wrong with his stub
        //pingThread is going to find it and send a quit message to model
        }

    }

    public Set<Double> getClients() throws RemoteException{
        return IdClientBindingMap.keySet();
    }


}

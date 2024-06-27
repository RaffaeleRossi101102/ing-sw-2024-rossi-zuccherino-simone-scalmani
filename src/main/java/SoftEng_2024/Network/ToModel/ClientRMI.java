package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.View;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Represents a client using RMI (Remote Method Invocation) to communicate with a server.
 * Implements the {@link ClientInterface} interface.
 */
public class ClientRMI extends UnicastRemoteObject implements ClientInterface {

    private View view;
    private ServerInterface server;
    public volatile LinkedBlockingQueue<ModelMessage> modelQueue;
    double ID;
    private final String ip;
    private final int port;

    /**
     * Constructs a new ClientRMI object.
     *
     * @param ID   The unique identifier for the client.
     * @param ip   The IP address of the RMI server.
     * @param port The port number for RMI communication.
     * @throws RemoteException If there is an RMI-related exception.
     */
    public ClientRMI(double ID ,String ip, int port) throws RemoteException {
        this.modelQueue=new LinkedBlockingQueue<>();
        this.ID = ID;
        this.ip=ip;
        this.port=port;
    }

    /**
     * Runs the client's execution logic, continuously processing messages from the model queue.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized void run() throws InterruptedException {
        while(true){
            //se andasse in wait anche se non ha la coda vuota???
            if(modelQueue.isEmpty()){
              wait();
            }else {
                //System.out.println("the message executing is: "+modelQueue.peek());
                modelQueue.take().executeMessage(this.view);
                //System.out.println("executed Message:");
            }
        }
    }

    /**
     * Updates the client's view with the provided message.
     *
     * @param msg The message containing information to update the view.
     * @throws RemoteException If there is an RMI-related exception.
     */
    @Override
    public  void update(ViewMessage msg) throws RemoteException {
        server.addToNetworkManager(msg);

    }

    /**
     * Signals the server that the client wishes to quit.
     *
     * @param msg The message indicating the reason for quitting.
     * @throws RemoteException If there is an RMI-related exception.
     */
    @Override
    public void quit(ViewMessage msg) throws RemoteException {

    }

    /**
     * Adds a model message to the client's view queue.
     *
     * @param msg The model message to be added to the client's view queue.
     * @throws RemoteException If there is an RMI-related exception.
     */
    @Override
    public synchronized void addToViewQueue(ModelMessage msg) throws RemoteException {

        modelQueue.add(msg);
        notifyAll();
    }

    /**
     * Registers the client to the server with a unique ID.
     *
     * @param ID     The unique identifier for the client.
     * @param client The client interface instance to register with the server.
     * @throws RemoteException   If there is an RMI-related exception.
     * @throws NotBoundException If the server is not bound to this client.
     */
    public void registerToServer(double ID, ClientInterface client) throws RemoteException, NotBoundException {
        //lookup on the registry
        Registry registry = LocateRegistry.getRegistry(ip, port);
        this.server = (ServerInterface) registry.lookup("RMI_server");
        server.registerClient(ID, client);
    }


    /**
     * Pings the server to confirm connectivity.
     *
     * @throws RemoteException If there is an RMI-related exception.
     */
    @Override
    public void pong() throws RemoteException{
    }



    /**
     * Sets the view associated with the client.
     *
     * @param view The view object to associate with the client.
     */
    public void setView(View view) {
        this.view = view;
    }
}

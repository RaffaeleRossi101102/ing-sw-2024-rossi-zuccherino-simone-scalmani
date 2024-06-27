package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;


import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.Set;

/**
 * Remote interface for the server in a client-server architecture using RMI.
 * Defines methods that the server can expose to clients.
 */
public interface ServerInterface extends Remote {
    /**
     * Adds a view message to the network manager.
     *
     * @param msg The view message to be added.
     * @throws RemoteException If a network error occurs during the method invocation.
     */
    void addToNetworkManager(ViewMessage msg) throws RemoteException;
    /**
     * Starts the server execution, potentially binding it to a network port.
     *
     * @throws RemoteException       If a network error occurs during the method invocation.
     * @throws AlreadyBoundException If the server is already bound to the specified network port.
     */
    void run() throws RemoteException, AlreadyBoundException;
    /**
     * Registers a client with a unique ID and its corresponding remote interface.
     *
     * @param ID     The unique ID of the client.
     * @param client The remote interface of the client.
     * @throws RemoteException If a network error occurs during the method invocation.
     */
    void registerClient(double ID, ClientInterface client) throws RemoteException;
    /**
     * Unregisters a client identified by the given ID.
     *
     * @param ID The unique ID of the client to unregister.
     * @throws RemoteException If a network error occurs during the method invocation.
     */
    void unregisterClient(double ID) throws RemoteException;
    /**
     * Adds a model message to the queue for processing by clients.
     *
     * @param msg The model message to be added to the client queue.
     * @throws RemoteException If a network error occurs during the method invocation.
     */
    void addToClientQueue(ModelMessage msg) throws RemoteException;
    /**
     * Retrieves the set of currently registered client IDs.
     *
     * @return A set containing the IDs of all registered clients.
     * @throws RemoteException If a network error occurs during the method invocation.
     */
    Set<Double> getClients() throws RemoteException;
}

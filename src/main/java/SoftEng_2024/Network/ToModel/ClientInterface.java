package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.View;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This interface defines the methods that a client can invoke remotely on a server.
 * It extends the Remote interface to indicate that these methods can be invoked remotely.
 */
public interface ClientInterface extends Remote {

    /**
     * Updates the client's view with the provided message.
     *
     * @param msg The message containing information to update the view.
     * @throws RemoteException If there is a communication-related exception during the remote method invocation.
     */
    void update(ViewMessage msg) throws RemoteException;

    /**
     * Signals the server that the client wishes to quit.
     *
     * @param msg The message indicating the reason for quitting.
     * @throws IOException If there is an I/O related exception.
     */
    void quit(ViewMessage msg) throws IOException;

    /**
     * Adds a model message to the client's view queue.
     *
     * @param msg The model message to be added to the client's view queue.
     * @throws RemoteException If there is a communication-related exception during the remote method invocation.
     */
    void addToViewQueue(ModelMessage msg) throws RemoteException;

    /**
     * Initiates the client's execution logic.
     *
     * @throws RemoteException    If there is a communication-related exception during the remote method invocation.
     * @throws InterruptedException If the thread executing the method is interrupted.
     */
    void run() throws RemoteException, InterruptedException;

    /**
     * Registers the client to the server with a unique ID.
     *
     * @param ID     The unique identifier for the client.
     * @param client The client interface instance to register with the server.
     * @throws RemoteException   If there is a communication-related exception during the remote method invocation.
     * @throws NotBoundException If the server is not bound to this client.
     */
    void registerToServer(double ID, ClientInterface client) throws RemoteException, NotBoundException;

    /**
     * Sets the view associated with the client.
     *
     * @param view The view object to associate with the client.
     * @throws RemoteException If there is a communication-related exception during the remote method invocation.
     */
    void setView(View view) throws RemoteException;

    /**
     * Pings the server to confirm connectivity.
     *
     * @throws RemoteException If there is a communication-related exception during the remote method invocation.
     */
    void pong() throws RemoteException;

}

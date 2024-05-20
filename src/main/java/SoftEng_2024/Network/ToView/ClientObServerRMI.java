package SoftEng_2024.Network.ToView;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.View;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientObServerRMI extends UnicastRemoteObject implements ObServerInterface {
    private View view;
    RMIObServer server;
    public ClientObServerRMI(RMIObServer stub) throws RemoteException {
        this.server = stub;
    }
    public void addToViewQueue(ModelMessage msg) throws RemoteException{}
}

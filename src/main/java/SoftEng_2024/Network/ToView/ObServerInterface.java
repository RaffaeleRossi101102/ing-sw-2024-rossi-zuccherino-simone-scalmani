package SoftEng_2024.Network.ToView;

import SoftEng_2024.Model.ModelMessages.ModelMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObServerInterface extends Remote {
    void addToViewQueue(ModelMessage msg) throws RemoteException;
}

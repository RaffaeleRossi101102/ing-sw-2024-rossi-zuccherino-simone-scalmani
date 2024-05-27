package SoftEng_2024.Network.ToView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

import SoftEng_2024.Model.ModelMessages.*;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToModel.SocketClientHandler;

public class ObServerManager {

    private LinkedBlockingQueue<ModelMessage> modelMessages;
    private ServerInterface serverRMI;
    private SocketServer socketServer;
    private SocketClientHandler socketServerHandler;

    public ObServerManager(ServerInterface serverRMI, SocketServer socketServer, SocketClientHandler socketServerHandler){
        this.serverRMI = serverRMI;
        this.socketServer = socketServer;
        this.socketServerHandler = socketServerHandler;
    }


    public void run() throws IOException {
    ModelMessage msg;
        while(true){
            try {
                msg=modelMessages.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            serverRMI.addToClientQueue(msg);
            socketServerHandler.addToClientQueue(msg);
        }
    }
}

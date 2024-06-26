package SoftEng_2024.Network.ToView;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import SoftEng_2024.Model.ModelMessages.*;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketServer;
/**
 * Manages incoming ModelMessages from various sources (RMI or SocketServer)
 * and distributes them accordingly.
 */
public class ObServerManager {

    private LinkedBlockingQueue<ModelMessage> modelMessages;
    private ServerInterface serverRMI;
    private SocketServer socketServer;

    /**
     * Constructs an ObServerManager with references to ServerInterface and SocketServer.
     *
     * @param serverRMI    The ServerInterface instance handling RMI communications.
     * @param socketServer The SocketServer instance handling socket communications.
     */
    public ObServerManager(ServerInterface serverRMI, SocketServer socketServer){
        this.serverRMI = serverRMI;
        this.socketServer = socketServer;
        modelMessages = new LinkedBlockingQueue<>();

    }

    /**
     * Continuously runs to process incoming ModelMessages from the queue,
     * distributing each message to ServerInterface and SocketServer for further handling.
     *
     * @throws IOException              If an I/O error occurs while processing messages.
     * @throws InterruptedException     If the thread is interrupted while waiting.
     */
    public synchronized void run() throws IOException, InterruptedException {
    ModelMessage msg;
        while(true) {
            if (modelMessages.isEmpty()) {
                wait();
            }else{
                try {
                    msg = modelMessages.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                serverRMI.addToClientQueue(msg);
                
                socketServer.addToClientQueue(msg);
            }
        }
    }
    /**
     * Adds a ModelMessage to the queue for processing.
     *
     * @param msg The ModelMessage to add to the queue.
     */
    public synchronized void addModelMessageToQueue(ModelMessage msg){
        modelMessages.add(msg);
        notifyAll();
    }
    /**
     * Retrieves the ServerInterface instance associated with this manager.
     *
     * @return The ServerInterface instance.
     */
    public ServerInterface getServerRMI() {
        return serverRMI;
    }
    /**
     * Retrieves the SocketServer instance associated with this manager.
     *
     * @return The SocketServer instance.
     */
    public SocketServer getSocketServer() {
        return socketServer;
    }
}

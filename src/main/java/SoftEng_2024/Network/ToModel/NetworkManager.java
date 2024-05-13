package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.View.Messages.MessageView;

import java.util.concurrent.LinkedBlockingQueue;

public class NetworkManager {
    GameController controller;
    LinkedBlockingQueue<MessageView> viewMessages;
    boolean running;

    public NetworkManager(GameController controller){
        this.controller = controller;
        this.viewMessages = new LinkedBlockingQueue<>();
    }


    public void run(){
        while(running){
            Thread t = new Thread(() -> {
                try {
                    viewMessages.take().executeMessage(this.controller);
                } catch (InterruptedException e) {
                    System.err.println("Something went wrong while executing the messages");
                    throw new RuntimeException(e);
                }
            });
            t.start();
        }
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    public void addViewMessages(MessageView msg) {
        viewMessages.add(msg);
    }
}

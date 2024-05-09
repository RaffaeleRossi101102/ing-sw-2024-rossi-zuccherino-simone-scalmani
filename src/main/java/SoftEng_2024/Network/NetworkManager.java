package SoftEng_2024.Network;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Controller.GameController;
import SoftEng_2024.View.MessageView;

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
            try {
                //Thread t= new Thread()
                viewMessages.take().executeMessage();
            } catch (InterruptedException e) {
                System.err.println("Something went wrong while executing the messages");
                throw new RuntimeException(e);
            }
        }
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    public void addViewMessages(MessageView msg) {
        viewMessages.add(msg);
    }
}

package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkManager {
    private final GameController controller;
    private LinkedBlockingQueue<ViewMessage> viewMessages;

    public NetworkManager(GameController controller){
        this.controller = controller;
        this.viewMessages = new LinkedBlockingQueue<>();
    }


    public synchronized void run() throws InterruptedException, IOException {
        System.out.println("Executing messages from the queue");
        controller.setNetworkManager(this);
        controller.gameInit();
        while(controller.getGame().getGameState()==GameState.CONNECTION){
            pollThreaded();
        }

        //exiting the loop only after every player has connected
        //vengono pescate le carte risorsa e oro e messe nel centro


        //viene data a ciascun player la carta iniziale

        controller.handOutStarterCards();

        //Each player plays their starter card and choose the color of his pawn
        while(controller.getGame().getGameState()==GameState.STARTER | controller.getGame().getGameState()==GameState.SETCOLOR){
            pollThreaded();
        }


        controller.updatePublicGoals();
        //I goal privati vengono aggiunti al player già nella addPlayer, da valutare se serve il metodo
        //oppure se tenerlo lì
        controller.handOutPrivateGoals();
        //Each player choose his private goal

        while(controller.getGame().getGameState()==GameState.CHOOSEGOAL){
            pollThreaded();
        }
        controller.getGame().updatePublicCards();


        //shuffle the players
        controller.getGame().shufflePlayers();
        //vengono date le carte a tutti i giocatori
        controller.handOutCards();
        controller.getGame().turnStart();
        //TODO: potrebbe mandare un messaggio in cui dice chi è di turno? Oppure lo si manda direttamente con
        //TODO: il messaggio di cambio stato
        //Each player is now initialized, and we are ready to start the game
        while(controller.getGame().getGameState()==GameState.PLAY){
            pollThreaded();
        }



        //when out of while loop it means that someone arrived at 20 points and users can only use the chat
        controller.getGame().gameEnd();


    }

    private void pollThreaded() throws InterruptedException {
        if (viewMessages.isEmpty()) {
            this.wait();
        } else {
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

    public synchronized void addViewMessages(ViewMessage msg) {
        viewMessages.add(msg);
        notifyAll();
    }

    public synchronized void wakeUpManager(){
        notifyAll();
    }

}

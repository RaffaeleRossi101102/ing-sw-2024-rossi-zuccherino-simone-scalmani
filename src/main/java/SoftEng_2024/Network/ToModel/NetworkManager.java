package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.concurrent.LinkedBlockingQueue;

public class NetworkManager {
    GameController controller;
    LinkedBlockingQueue<ViewMessage> viewMessages;
    boolean running;

    public NetworkManager(GameController controller){
        this.controller = controller;
        this.viewMessages = new LinkedBlockingQueue<>();
    }


    public synchronized void run() throws InterruptedException {
        System.out.println("Executing messages from the queue");
        controller.setNetworkManager(this);
        controller.gameInit();
        while(controller.getGame().getGameState()==GameState.CONNECTION){
            pollThreaded();
        }
        //exiting the loop only after every player has connected
        System.err.println("sono uscito, asgarra");
        //vengono pescate le carte risorsa e oro e messe nel centro
        controller.getGame().updatePublicCards();
        //viene data a ciascun player la carta iniziale

        controller.handOutStarterCards();


        //Each player plays their starter card and choose the color of his pawn
        while(controller.getGame().getGameState()==GameState.STARTER | controller.getGame().getGameState()==GameState.SETCOLOR){
            pollThreaded();
        }

        //I goal privati vengono aggiunti al player già nella addPlayer, da valutare se serve il metodo
        //oppure se tenerlo lì
        controller.handOutPrivateGoals();
        //Each player choose his private goal
        while(controller.getGame().getGameState()==GameState.CHOOSEGOAL){
            pollThreaded();
        }
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
        //TODO ritornare ad inizio run per la creazione di una nuova partita??

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

    public void setRunning(boolean running){
        this.running = running;
    }

    public synchronized void addViewMessages(ViewMessage msg) {
        viewMessages.add(msg);
        notifyAll();
    }
    public synchronized void wakeUpManager(){
        notifyAll();
    }

}

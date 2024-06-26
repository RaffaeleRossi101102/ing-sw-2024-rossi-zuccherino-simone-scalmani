package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Manages network communication and synchronization between the game controller and views.
 */
public class NetworkManager {

    private final GameController controller;
    private LinkedBlockingQueue<ViewMessage> viewMessages;

    /**
     * Constructs a NetworkManager object.
     *
     * @param controller The GameController instance managing the game state.
     */
    public NetworkManager(GameController controller){
        this.controller = controller;
        this.viewMessages = new LinkedBlockingQueue<>();
    }

    /**
     * Runs the network manager, processing messages and synchronizing game state.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws IOException          If there is an I/O related exception.
     */
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

    /**
     * Processes messages from the view messages queue in a separate thread.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
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

    /**
     * Adds a view message to the manager's queue and wakes up the manager if it is waiting.
     *
     * @param msg The view message to add to the queue.
     */
    public synchronized void addViewMessages(ViewMessage msg) {
        viewMessages.add(msg);
        notifyAll();
    }

    /**
     * Wakes up the network manager thread if it is waiting.
     */
    public synchronized void wakeUpManager(){
        notifyAll();
    }

}

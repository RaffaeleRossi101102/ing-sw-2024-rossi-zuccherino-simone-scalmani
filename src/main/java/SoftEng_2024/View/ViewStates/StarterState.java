package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.PlayStarterCardMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * StarterState represents the state where players play their starter card at the beginning of the game.
 * Players can interact by playing their starter card, chatting, or quitting.
 */
public class StarterState extends ViewState{
    /**
     * Constructs a StarterState object.
     *
     * @param view   The CliViewClient associated with this state.
     * @param client The ClientInterface through which interactions with the game server occur.
     * @param ID     The unique identifier of the player in the game.
     */
    public StarterState(CliViewClient view, ClientInterface client,double ID){
        super(view,client,ID);
    }
    /**
     * Displays the starter state, allowing the player to play their starter card.
     *
     * @throws InterruptedException If the thread is interrupted while waiting for player actions.
     */
    @Override
    public synchronized void display() throws InterruptedException {
        String indications="Type  Play Starter Card, Chat or Quit";
        listenDefaultCommand(true);
        defaultCommand(GameState.CONNECTION,"Waiting for all the players to connect");
        System.out.println("Now it's time to play your starter card:");
        //TODO: prints player's starter card
        //Scanner scanner = new Scanner(System.in);
        System.out.println(indications);
        wait();

        //loops until the player chooses a command different from writeInChat
        while(!commandChosen){
            switch(view.getCommand().trim().replaceAll("\\s+", "").toLowerCase()) {
                case "playstartercard":
                    if(view.getLocalModel().getStarterCard()==null){
                        System.err.println("StarterCard isn't already available, wait a few seconds and retry...");
                        view.setCommand("");
                        listenDefaultCommand(true);
                        System.out.println(indications);
                        wait();
                    }
                    else {
                        System.out.println("This is your starter card:");
                        commandChosen=true;
                        playStarterCard();
                        if(!commandChosen) {
                            listenDefaultCommand(true);
                            System.out.println("Type Choose Private Goal, Show Board, Chat or Quit");
                            wait();
                        }
                        view.setCommand("");
                    }
                    break;
                case "chat":
                    writeInChat();

                    view.setCommand("");
                    listenDefaultCommand(true);
                    System.out.println(indications);
                    wait();

                    break;
                case "quit":
                    quit();

                    break;
                case "":
                    break;
                default:
                    System.err.println("Command not available... retry");
                    view.setCommand("");
                    listenDefaultCommand(true);
                    System.out.println(indications);
                    wait();
                    break;
            }
            if(commandChosen)
                break;


        }
        this.view.getWaitingState().setPreviousState(this);
        this.view.getWaitingState().setNextState(new SetColorState(view,client,ID));
    }
    /**
     * Allows the player to play their starter card by interacting with console input.
     * Sends a PlayStarterCardMessage to update the game state with the chosen side (front or back) of the card.
     */
    private void playStarterCard() {
        Scanner input= new Scanner(System.in);
        String answer;
        boolean flipped;
        System.out.println("This is your hand, the front is:");
        StarterCard card=view.getLocalModel().getStarterCard();
        String upperHand=card.displayGraphicCard()[0];
        String midHand=card.displayGraphicCard()[1];
        String downHand=card.displayGraphicCard()[2];
        System.out.println(upperHand);
        System.out.println(midHand);
        System.out.println(downHand);
        System.out.println();
        card.setFlipped(true);
        System.out.println("The back is: ");
        upperHand=card.displayGraphicCard()[0];
        midHand=card.displayGraphicCard()[1];
        downHand=card.displayGraphicCard()[2];
        System.out.println(upperHand);
        System.out.println(midHand);
        System.out.println(downHand);
        System.out.println();
        System.out.println("Type the side of the card (front or back), or type 'exit' to cancel");
        answer = input.nextLine().trim().replaceAll("\\s+", "").toLowerCase();
        while(!answer.equals("front") && !answer.equals("back") && !answer.equals("exit")){
             
            System.out.println("Wrong input... retry!\nType the side of the card (front or back), or type 'exit' to cancel");
            answer = input.nextLine();
        }
         
        if(answer.equals("exit")){
            commandChosen=false;
            return;
        }

        flipped= !answer.equals("front");
        ViewMessage msg= new PlayStarterCardMessage(flipped,this.ID);
        updateClient(msg);
    }


}

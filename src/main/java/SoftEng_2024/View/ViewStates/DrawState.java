package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.DrawFromPublicCardsMessage;
import SoftEng_2024.View.ViewMessages.DrawFromTheDeckMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Represents the state where a player can choose to draw a card from either the public cards or one of the decks.
 * Extends {@link ViewState}.
 */
public class DrawState extends ViewState{


    private ViewState nextState;
    /**
     * Constructs a DrawState instance with the specified CLI view client, client interface, and player ID.
     *
     * @param view   The CLI view client managing the state transitions.
     * @param client The client interface used for communication with the game server.
     * @param ID     The ID of the player associated with this state.
     */
    public DrawState(CliViewClient view, ClientInterface client, double ID) {
        super(view,client,ID);
    }
    /**
     * Displays the options for drawing a card from the public cards or the decks.
     * Waits for user input and processes commands until a valid command is chosen.
     *
     * @throws InterruptedException If the thread is interrupted while waiting for input.
     */
    @Override
    public synchronized void display() throws InterruptedException {
        if(view.getLocalModel().getState().equals(GameState.ENDGAME)){
            new EndGameState(view, client, ID).display();
        }else {
            String indications="Type Draw From Deck, Draw Public Card, Show Board, Show Hand, Chat or Quit";
            printPlayerBoard(true);
            showPublicGoals();
            showDecksAndPublicCards();
            System.out.println("Now, you have to draw a card from the 4 public cards or from one of the decks!");
            System.out.println(indications);
            wait();
            while (!commandChosen) {
                switch (view.getCommand().trim().replaceAll("\\s+", "").toLowerCase()) {
                    case "drawfromdeck":
                        commandChosen = true;

                        drawFromTheDeck();
                        view.setCommand("");
                        listenDefaultCommand(true);
                        if(!commandChosen) {
                            System.out.println(indications);
                            wait();
                        }
                        break;
                    case "drawpubliccard":
                        commandChosen = true;

                        drawPublicCard();
                        view.setCommand("");
                        listenDefaultCommand(true);
                        if(!commandChosen) {
                            System.out.println(indications);
                            wait();
                        }
                        break;
                    case "showboard":
                        printPlayerBoard(false);
                        view.setCommand("");
                        listenDefaultCommand(true);
                        wait();
                        break;
                    case "showhand":
                        showHand();
                        System.out.println(indications);
                        view.setCommand("");
                        listenDefaultCommand(true);
                        wait();
                        break;
                    case "playerscore":
                        playersScore();
                        System.out.println(indications);
                        view.setCommand("");
                        listenDefaultCommand(true);
                        wait();
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
                        System.out.println("[ERROR] Command not available... retry");
                        view.setCommand("");
                        listenDefaultCommand(true);
                        System.out.println(indications);
                        wait();
                        break;
                }
                if (commandChosen) {
                    view.getLocalModel().setPlayerState(GameState.NOTPLAYING);
                    commandChosen=false;
                    break;
                }

            }
            this.view.getWaitingState().setPreviousState(this);
            this.view.getWaitingState().setNextState(nextState);
        }
    }
    /**
     * Sets the next state after this state.
     *
     * @param nextState The next state to be set.
     */
    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }
    /**
     * Handles the process of drawing a card from one of the decks (gold or resources).
     * Prompts the user for the deck choice and sends a message to update the client.
     */
    private void drawFromTheDeck(){
        Scanner input = new Scanner(System.in);
        String deck;
        int index;
        System.out.println("Type which deck (gold or resources) you want to draw from, or type 'exit' to cancel");
        deck = input.nextLine();
        while(!deck.equals("gold") && !deck.equals("resources") && !deck.equals("exit")){
            System.out.println("[ERROR] Wrong input... retry!!\nType which deck (gold or resources) you want to draw from, or type 'exit' to cancel");
            deck = input.nextLine();
        }
         
        if(deck.equals("exit")){
            commandChosen = false;
            return;
        }

        index = deck.equals("gold") ? 1 : 0;
        ViewMessage msg = new DrawFromTheDeckMessage(index, this.ID);
        updateClient(msg);

    }
    /**
     * Handles the process of drawing a card from the public cards.
     * Prompts the user for the card choice and sends a message to update the client.
     */
    private void drawPublicCard(){
        Scanner input=new Scanner(System.in);
        String answer;
        int card;

        System.out.println("Type which card you want to draw: [1,2] for resource cards [3,4] for gold cards, or type 'exit' to cancel ");
        answer = input.nextLine();

        while(!answer.equals("1") && !answer.equals("2") && !answer.equals("3") && !answer.equals("4") && !answer.equals("exit")){
             
            System.out.println("Wrong Input!\nType which card you want to draw: [1,2] for resource cards [3,4] for gold cards, or type 'exit' to cancel ");
            answer = input.nextLine();
        }
         
        if(answer.equals("exit")){
            commandChosen = false;
            return;
        }

        card = Integer.parseInt(answer);
        card--;
        ViewMessage msg= new DrawFromPublicCardsMessage(card,this.ID);
        updateClient(msg);

    }
}

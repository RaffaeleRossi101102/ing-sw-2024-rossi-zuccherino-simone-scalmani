package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.PlayCardMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * PlayState represents the state where a player can interactively play their turn in the game.
 * It allows the player to perform actions such as playing a card, viewing boards and hands,
 * chatting, and quitting the game.
 */
public class PlayState extends ViewState{



    private ViewState nextState;
    /**
     * Constructs a PlayState object.
     *
     * @param view   The CliViewClient associated with this state.
     * @param client The ClientInterface through which interactions with the game server occur.
     * @param ID     The unique identifier of the player in the game.
     */
    public PlayState(CliViewClient view, ClientInterface client, double ID){
        super(view,client,ID);
    }
    /**
     * Displays the play state, allowing the player to interact with the game by choosing actions
     * such as playing a card, viewing boards and hands, chatting, or quitting.
     *
     * @throws InterruptedException If the thread is interrupted while waiting for user input.
     */
    @Override
    public synchronized void display() throws InterruptedException {
        System.out.println("It's your turn!");
        String indications="Type Play Card to play a card, Show Board to see a player's board, Show Hand to see your hand, Show Playground to see public cards and decks, " +
                "PlayerScore to the the players' score,Chat or Quit";
        if(view.getLocalModel().getPersonalHand().size() == 3) {
//            Scanner scanner = new Scanner(System.in);
            System.out.println(indications);
            if(!defaultCommandChosen)
                wait();
//            String command = scanner.nextLine();
            //loops until the player chooses a command different from writeInChat
            while (!commandChosen) {
                switch (view.getCommand().trim().replaceAll("\\s+", "").toLowerCase()) {
                    case "playcard":
                        commandChosen = true;
                        playCard();
                        view.setCommand("");
                        listenDefaultCommand(true);
                        if(!commandChosen) {
                            System.out.println(indications);
                            wait();
                        }
                        break;
                    case "chat":
                        writeInChat();
                        System.out.println(indications);
                        view.setCommand("");
                        listenDefaultCommand(true);
                        wait();
                        break;
                    case "showboard":
                        printPlayerBoard(false);
                        view.setCommand("");
                        listenDefaultCommand(true);
                        System.out.println(indications);
                        wait();
                        break;
                    case "showhand":
                        showHand();
                        System.out.println(indications);
                        view.setCommand("");
                        listenDefaultCommand(true);
                        wait();
                        break;
                    case "showplayground":
                        showPublicGoals();
                        showDecksAndPublicCards();
                        view.setCommand("");
                        listenDefaultCommand(true);
                        System.out.println(indications);
                        wait();
                        break;
                    case "playerscore":
                        playersScore();
                        view.setCommand("");
                        listenDefaultCommand(true);
                        System.out.println(indications);
                        wait();
                        break;
                    case "quit":
                        quit();
                        break;
                    case"":
                        break;
                    default:
                        System.out.println("[ERROR] Command not available... retry");
                        System.out.println(indications);
                        view.setCommand("");
                        listenDefaultCommand(true);
                        wait();
                        break;
                }
                if(commandChosen){
                    commandChosen=false;
                    break;
                }
            }
            this.view.getWaitingState().setPreviousState(this);
            this.view.getWaitingState().setNextState(nextState);
        } else {
            System.out.println("When you logged off last time, you played a card but didn't draw another one. " +
                               "Now, you'll need to finish your turn by drawing a card without playing...");
            this.view.setViewState(nextState);
            nextState.display();


        }
    }
    /**
     * Sets the next state of this PlayState.
     *
     * @param nextState The next state to set.
     */
    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }
    /**
     * Allows the player to play a card by interacting with the console input.
     * Collects card index, target row and column, and whether the card is flipped or not,
     * then sends a PlayCardMessage to update the game state.
     */
    private void playCard(){
        Scanner input = new Scanner(System.in);
        int card;
        String answer;
        printPlayerBoard(true);
        showHand();
        System.out.println("Type the index (1, 2 or 3) of the card you want to play, or type 'exit' to cancel ");
        answer = input.nextLine();
        while(!answer.equals("1") && !answer.equals("2") && !answer.equals("3") && !answer.equals("exit")){
            System.out.println("[ERROR] Wrong input... retry!!\nType the index (1, 2 or 3) of the card you want to play, or type 'exit' to cancel");
            answer = input.nextLine();

        }
        if(answer.equals("exit")){
            commandChosen = false;
            return;
        }

        card = Integer.parseInt(answer);
        card--;

        int row;
        int column;
        System.out.println("Type the row and the column (0 <= r & c <= 84) of the cell one by one, or type 'exit' to cancel ");
        try {
            row = input.nextInt();
            column = input.nextInt();
        }catch(InputMismatchException e){
            row=-1;
            column=-1;
            String command = input.nextLine().trim().toLowerCase();
            if (command.equals("exit")){
                commandChosen = false;
                return;
            }
        }
        while(row<0 | row>84 | column<0 | column>84){
            System.out.println("[ERROR] Wrong input, type an integer between '0' and '84' either for the row and the column, or type 'exit' to cancel");
            try {
                row = input.nextInt();
                column = input.nextInt();
            }catch(InputMismatchException e){
                row=-1;
                column=-1;
                String command = input.nextLine().trim().toLowerCase();
                if (command.equals("exit")){
                    commandChosen = false;
                    return;
                }
            }
        }
        boolean flipped;
        System.out.println("Type the side of the card (front or back), or type 'exit' to cancel ");
        input.nextLine();
        answer=input.nextLine();
        while(!answer.equals("front") && !answer.equals("back") && !answer.equals("exit")){
             
            System.out.println("[ERROR] Wrong input... retry!! \nType the side of the card (front or back), or type 'exit' to cancel");
            answer = input.nextLine();
        }
        if(answer.equals("exit")){
            commandChosen = false;
            return;
        }

        flipped = !answer.equals("front");

        ViewMessage msg = new PlayCardMessage(card, row, column, flipped, this.ID);
        updateClient(msg);
    }
}

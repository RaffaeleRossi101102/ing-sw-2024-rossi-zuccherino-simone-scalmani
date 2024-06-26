package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.ChoosePrivateGoalMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.Scanner;
/**
 * Represents the state where a player chooses their private goal in the game.
 * Extends {@link ViewState}.
 */
public class ChooseGoalState extends ViewState {
    /**
     * Constructs a ChooseGoalState instance with the specified view, client interface, and player ID.
     *
     * @param view   The CLI view client managing the state transitions.
     * @param client The client interface used for communication with the game server.
     * @param ID     The ID of the player associated with this state.
     */
    public ChooseGoalState(CliViewClient view, ClientInterface client, double ID){
        super(view,client,ID);
    }
    /**
     * Displays the state's options and handles user input for choosing a private goal.
     * Waits for user input and processes commands until a valid choice is made.
     *
     * @throws InterruptedException If interrupted while waiting for user input.
     */
    @Override
    public synchronized void display() throws InterruptedException {
        String indications="Type Choose Private Goal, Show Board, Chat or Quit";
        defaultCommand(GameState.SETCOLOR,"Waiting for all the players to choose a color");
        System.out.println("Now it's time to choose your private goal!");
        System.out.println(indications);
        wait();
        //loops until the player chooses a command different from writeInChat
        while (!commandChosen) {
            switch (view.getCommand().trim().replaceAll("\\s+", "").toLowerCase()) {
                case "chooseprivategoal":
                    if (view.getLocalModel().getAvailableGoals().isEmpty()){
                        System.err.println("Goals are not already available, wait a few seconds and retry...");
                        view.setCommand("");
                        listenDefaultCommand(true);
                        System.out.println(indications);
                        wait();

                    }else{
                        commandChosen = true;
                        choosePrivateGoal();
                        if(!commandChosen) {
                            listenDefaultCommand(true);
                            System.out.println(indications);
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
                case "showboard":
                    printPlayerBoard(false);
                    view.setCommand("");
                    listenDefaultCommand(true);
                    System.out.println(indications);
                    wait();
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
        this.view.getWaitingState().setNextState(new ReadyToStartState(view,client, ID));
    }
    /**
     * Handles the process of choosing a private goal by displaying options and updating the client.
     */
    private void choosePrivateGoal() {
        System.out.println("Inside choose private goal...");
        Scanner scanner = new Scanner(System.in);
        String answer;
        System.out.println("Goal 1: " + view.getLocalModel().getAvailableGoals().get(0).getGoalType());
        System.out.println("Goal 2: " + view.getLocalModel().getAvailableGoals().get(1).getGoalType());
        System.out.println("Type 1 or 2 to choose your private goal, or type exit to cancel");
        answer = scanner.nextLine();
        while (!answer.equals("1") && !answer.equals("2") && !answer.equals("exit")) {
             
            System.err.println("Wrong input, type [1] or [2] to choose your private goal");
            answer = scanner.nextLine();
        }
         
        if (answer.equals("exit")) {
            commandChosen = false;
            return;
        }
        int choice = Integer.parseInt(answer);
        System.out.println("Got it, keep it a secret!");
        ViewMessage msg= new ChoosePrivateGoalMessage(choice,ID);
        updateClient(msg);
    }
}

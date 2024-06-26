package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.ChoosePrivateGoalMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.Scanner;

public class ChooseGoalState extends ViewState {
    public ChooseGoalState(CliViewClient view, ClientInterface client, double ID){
        super(view,client,ID);
    }

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

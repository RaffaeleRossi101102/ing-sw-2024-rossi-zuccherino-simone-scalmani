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
    public void display() {
        System.out.println("Waiting for all the players to choose their color...");
        defaultCommand(GameState.SETCOLOR);
        System.out.println("Now it's time to choose your private goal!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type Choose Private Goal, Chat or Quit");
        String command = scanner.nextLine();
        //loops until the player chooses a command different from writeInChat
        while (!commandChosen) {
            switch (command.trim().toLowerCase()) {
                case "chooseprivategoal":
                    if (view.getLocalModel().getAvailableGoals().isEmpty()){
                        System.err.println("Goals are not already available, wait a few seconds and retry...");
                    }else{
                        choosePrivateGoal();
                        commandChosen = true;
                    }
                    break;
                case "chat":
                    writeInChat();
                    break;
                case "quit":
                    quit();
                    break;
                default:
                    System.err.println("Command not available... retry");
                    break;
            }
            System.out.println("Type Choose Private Goal, Chat or Quit");
            command = scanner.nextLine();
        }
        waitingState.setPreviousState(this);
        waitingState.setNextState(new PlayState(view,client, ID));
        waitingState.display();
    }

    private void choosePrivateGoal() {
        System.out.println("Inside choose private goal...");
        Scanner scanner = new Scanner(System.in);
        String answer;
        System.out.println("Goal 1: " + view.getLocalModel().getAvailableGoals().get(0).getGoalType());
        System.out.println("Goal 2: " + view.getLocalModel().getAvailableGoals().get(1).getGoalType());
        System.out.println("Type [1] or [2] to choose your private goal");
        answer = scanner.nextLine();
        while (!answer.equals("1") && !answer.equals("2")) {
            System.err.println("Wrong input, type [1] or [2] to choose your private goal");
            answer = scanner.nextLine();
        }
        int choice = Integer.parseInt(answer);
        System.out.println("Got it, keep it a secret!");
        ViewMessage msg= new ChoosePrivateGoalMessage(choice,ID);
        updateClient(msg);
    }
}

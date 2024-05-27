package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;

import java.util.Scanner;

public class PlayState extends ViewState{
    public PlayState(CliViewClient view, ClientInterface client, double ID){
        super(view,client,ID);
    }

    @Override
    public void display() {
        System.out.println("Waiting for all the players to choose their private goal...");
        defaultCommand(GameState.CHOOSEGOAL);
        //TODO System.out.println("Now it's time to Play! It's "+ currentPlayer+ " turn");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type ChoosePrivateGoal  or chat");
        String command = scanner.nextLine();
        //TODO: e se ci fosse un while in cui si aspetta di diventare di turno per giocare? O in generale dentro al metodo
        //TODO: play e draw card ci potrebbe essere un controllo diretto se si è di turno
        //loops until the player chooses a command different from writeInChat
        while (!commandChosen) {
            switch (command.trim().toLowerCase()) {
                case "chooseprivategoal":
                   // choosePrivateGoal();
                    commandChosen = true;
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
            System.out.println("Type  'Set Color' or 'Chat'");
            command = scanner.nextLine();
        }
        waitingState.setPreviousState(this);
        waitingState.setNextState(new PlayState(view,client, ID));
        waitingState.display();
    }


}

package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.PlayStarterCardMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.Scanner;

public class StarterState extends ViewState{
    public StarterState(CliViewClient view, ClientInterface client,double ID){
        super(view,client,ID);
    }
    @Override
    public void display() {
        System.out.println("Waiting for players to connect...");
        defaultCommand(GameState.CONNECTION);
        System.out.println("Now it's time to play your starter card:");
        //TODO: prints player's starter card
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type  Play Starter Card, Chat or Quit");
        String command = scanner.nextLine();
        //loops until the player chooses a command different from writeInChat
        while(!commandChosen){
            switch(command.trim().toLowerCase()) {
                case "playstartercard":
                    if(view.getLocalModel().getStarterCard()==null){
                        System.err.println("StarterCard isn't already available, wait a few seconds and retry...");
                    }
                    else {
                        playStarterCard();
                        commandChosen=true;
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
            if(commandChosen)
                break;
            System.out.println("Type  Play starter card  or chat");
            command=scanner.nextLine();
        }
        waitingState.setPreviousState(this);
        waitingState.setNextState(new SetColorState(view,client,ID));
        waitingState.display();
    }
    private void playStarterCard() {
        Scanner input= new Scanner(System.in);
        String answer;
        boolean flipped;
        //TODO: Print delle starterCard
        do {
            System.out.println("Type the side of the card (front or back): ");
            answer = input.nextLine();
        }while(!answer.equals("front") && !answer.equals("back"));
        flipped= !answer.equals("front");
        ViewMessage msg= new PlayStarterCardMessage(flipped,this.ID);
        updateClient(msg);
    }

}

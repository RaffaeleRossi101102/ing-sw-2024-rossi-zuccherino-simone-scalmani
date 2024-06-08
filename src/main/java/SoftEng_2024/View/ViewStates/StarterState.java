package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.PlayStarterCardMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        startTimer(entryTimer);
        String command = scanner.nextLine();
        //loops until the player chooses a command different from writeInChat
        while(!commandChosen){
            switch(command.trim().replaceAll("\\s+", "").toLowerCase()) {
                case "playstartercard":
                    if(view.getLocalModel().getStarterCard()==null){
                         
                        System.err.println("StarterCard isn't already available, wait a few seconds and retry...");
                    }
                    else {
                         
                        commandChosen=true;
                        playStarterCard();
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
            System.out.println("Type  Play Starter Card, Chat or Quit");
            System.out.println("Il thread del client è in: "+view.clientQueueExecutor.getState());
            command=scanner.nextLine();
        }
        this.view.getWaitingState().setPreviousState(this);
        this.view.getWaitingState().setNextState(new SetColorState(view,client,ID));
    }
    private void playStarterCard() {
        Scanner input= new Scanner(System.in);
        String answer;
        boolean flipped;
        //TODO: Print delle starterCard
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

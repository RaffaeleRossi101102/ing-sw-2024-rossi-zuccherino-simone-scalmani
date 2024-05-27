package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.SetColorMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.Scanner;

public class SetColorState extends ViewState{
    public SetColorState(CliViewClient view, ClientInterface client,double ID){
        super(view,client,ID);
    }

    @Override
    public void display() {
        System.out.println("Waiting for all the players to play their starter card...");
        defaultCommand(GameState.STARTER);
        System.out.println("Everyone played their starter card! Now you all have to choose your color!");
        //TODO: valutare se stampare tutti i colori o solo quelli disponibili (in caso di fallimento)
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type  set color, chat or quit");
        String command = scanner.nextLine();
        //loops until the player chooses a command different from writeInChat
        while(!commandChosen){
            switch(command.trim().toLowerCase()) {
                case "setcolor":
                    setColor();
                    commandChosen=true;
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
            System.out.println("Type  Set color, chat or quit");
            command=scanner.nextLine();
        }
        waitingState.setPreviousState(this);
        waitingState.setNextState(new ChooseGoalState(view,client,ID));
        waitingState.display();
    }
    private void setColor() {
        Scanner input = new Scanner(System.in);
        String answer;
        Color color;
        System.out.println("Type the color (RED,BLUE,GREEN,YELLOW) you want for your pawn");
        answer = input.nextLine();
        while(!answer.equals("RED") && !answer.equals("BLUE") && !answer.equals("YELLOW") && !answer.equals("GREEN")){
            System.err.println("Wrong input, please choose between the colors: [RED,BLUE,GREEN,YELLOW]");
            answer=input.nextLine();
        }
        color = Color.valueOf(answer);
        ViewMessage msg = new SetColorMessage(color, this.ID);
        updateClient(msg);
    }
}

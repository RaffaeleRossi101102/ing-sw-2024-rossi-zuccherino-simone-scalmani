package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.SetColorMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class SetColorState extends ViewState{
    public SetColorState(CliViewClient view, ClientInterface client,double ID){
        super(view,client,ID);
    }

    @Override
    public void display() {
        listenDefaultCommand();
        defaultCommand(GameState.STARTER,"Waiting for all the players play their starter card...");
        System.out.println("Everyone played their starter card! Now you all have to choose your color!");
        System.out.println("Type  Set Color, Show Board, Chat or Quit");

        //loops until the player chooses a command different from writeInChat
        while(!commandChosen){
            switch(view.getCommand().trim().replaceAll("\\s+", "").toLowerCase()) {
                case "setcolor":
                    commandChosen=true;
                    setColor();
                    view.setCommand("");
                    listenDefaultCommand();
                    break;
                case "chat":
                    writeInChat();
                    System.out.println("Type  Set Color, Show Board, Chat or Quit");
                    view.setCommand("");
                    listenDefaultCommand();
                    break;
                case "showboard":
                    printPlayerBoard();
                    view.setCommand("");
                    listenDefaultCommand();
                    System.out.println("Type  Set Color, Show Board, Chat or Quit");
                    break;
                case "quit":
                    quit();
                    break;
                case "":
                    break;
                default:
                    System.err.println("Command not available... retry");
                    System.out.println("Type  Set Color, Show Board, Chat or Quit");
                    view.setCommand("");
                    listenDefaultCommand();
                    break;
            }
            if(commandChosen)
                break;

        }
        this.view.getWaitingState().setPreviousState(this);
        this.view.getWaitingState().setNextState(new ChooseGoalState(view,client,ID));

    }
    private void setColor() {
        Scanner input = new Scanner(System.in);
        String answer;
        Color color;
        ConcurrentHashMap<String, Color> colorMap = view.getLocalModel().getPlayersColor();
        System.out.println("The available colors are: ");
        for(Color availableColor : Color.values()) {
            if(!colorMap.containsValue(availableColor) && !availableColor.equals(Color.BLACK) & !availableColor.equals(Color.EMPTY)){
                System.out.print(availableColor+ " ");
            }
        }
        System.out.println("Type the color you want for your pawn, or type 'exit' to cancel");
        answer = input.nextLine().trim().replaceAll("\\s+", "").toUpperCase();
        boolean correctColor = false;
        while (!correctColor) {
            if(answer.equals("EXIT")){
                 
                commandChosen=false;
                return;
            }
            else if ((answer.equals("RED") | answer.equals("BLUE") | answer.equals("YELLOW") | answer.equals("GREEN")) && !colorMap.containsValue(Color.valueOf(answer))) {
                correctColor = true;
            }else if(!answer.equals("RED") && !answer.equals("BLUE") && !answer.equals("YELLOW") && !answer.equals("GREEN")){
                System.err.println("Wrong input, please choose between the colors: Red , Blue, Green, Yellow");
                 
                answer = input.nextLine().trim().replaceAll("\\s+", "").toUpperCase();
            } else if (colorMap.containsValue(Color.valueOf(answer))){
                System.err.println("Type a correct color, but it has been already chosen by someone else... retry");
                 
                answer = input.nextLine().trim().replaceAll("\\s+", "").toUpperCase();
            }
        }
         
        color = Color.valueOf(answer);
        ViewMessage msg = new SetColorMessage(color, this.ID);
        updateClient(msg);
    }
}

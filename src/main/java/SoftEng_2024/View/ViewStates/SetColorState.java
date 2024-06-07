package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.SetColorMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.HashMap;
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type  Set Color, Chat or Quit");
        startTimer(entryTimer);
        String command = scanner.nextLine();
        //loops until the player chooses a command different from writeInChat
        while(!commandChosen){
            switch(command.trim().replaceAll("\\s+", "").toLowerCase()) {
                case "setcolor":
                    commandChosen=true;
                    resetTimer();
                    setColor();
                    break;
                case "chat":
                    resetTimer();
                    writeInChat();
                    break;
                case "quit":
                    resetTimer();
                    quit();
                    break;
                default:
                    resetTimer();
                    System.err.println("Command not available... retry");
                    break;
            }
            if(commandChosen)
                break;
            resetTimer();
            System.out.println("Type  Set color, chat or quit");
            command=scanner.nextLine();
        }
        waitingState.setPreviousState(this);
        waitingState.setNextState(new ChooseGoalState(view,client,ID));

        Thread newStateDisplayThread = new Thread(waitingState::display);
        newStateDisplayThread.start();
    }
    private void setColor() {
        Scanner input = new Scanner(System.in);
        String answer;
        Color color;
        HashMap<String, Color> colorMap = view.getLocalModel().getPlayersColor();
        System.out.println("The available colors are: ");
        for(Color availableColor : Color.values()) {
            if(!colorMap.containsValue(availableColor)){
                System.out.print(availableColor+ " ");
            }
        }
        resetTimer();
        System.out.println("Type the color you want for your pawn, or type 'exit' to cancel");
        answer = input.nextLine().trim().replaceAll("\\s+", "").toUpperCase();
        boolean correctColor = false;
        while (!correctColor) {
            if(answer.equals("EXIT")){
                resetTimer();
                commandChosen=false;
                return;
            }
            else if ((answer.equals("RED") | answer.equals("BLUE") | answer.equals("YELLOW") | answer.equals("GREEN")) && !colorMap.containsValue(Color.valueOf(answer))) {
                correctColor = true;
            }else if(!answer.equals("RED") && !answer.equals("BLUE") && !answer.equals("YELLOW") && !answer.equals("GREEN")){
                System.err.println("Wrong input, please choose between the colors: Red , Blue, Green, Yellow");
                resetTimer();
                answer = input.nextLine().trim().replaceAll("\\s+", "").toUpperCase();
            } else if (colorMap.containsValue(Color.valueOf(answer))){
                System.err.println("Type a correct color, but it has been already chosen by someone else... retry");
                resetTimer();
                answer = input.nextLine().trim().replaceAll("\\s+", "").toUpperCase();
            }
        }
        resetTimer();
        color = Color.valueOf(answer);
        ViewMessage msg = new SetColorMessage(color, this.ID);
        updateClient(msg);
    }
}

package SoftEng_2024.View;


import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.Messages.*;

import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CliViewClient {
    ClientInterface client;

    double ID;
    public CliViewClient(double ID){
        this.ID=ID;
    }

    protected void run(){
        System.out.println("The CliView is running!");
        System.out.println("Type join or rejoin to play!");
        Scanner scanner= new Scanner(System.in);
        String command=scanner.nextLine();

        do {
            switch(command){
                case "CreateGame":
                    createGame();
                case "PlayCard":
                    playCard();
                case "DrawCard":
                    drawCard();
                case "Connect":
                    join();
                case "WriteInChat":
                    writeInChat();
                case "Reconnect":
                    rejoin();
                case "InitializePlayer":
                    initializePlayer();
                default:
                    System.err.println("Command not available... retry");
            }

            command=scanner.nextLine();

        }while (!command.equals("quit"));

        quit();

    }

    private void playStarterCard() {
        Scanner input= new Scanner(System.in);
        String answer;
        boolean flipped;
        do {
            System.out.println("Type the side of the card (front or back): ");
            answer = input.nextLine();
        }while(!answer.equals("front") && !answer.equals("back"));
        flipped= !answer.equals("front");
        MessageView msg= new PlayStarterCardMessage(flipped,this.ID);
        updateClient(msg);
    }

    private void initializePlayer() {
    }

    private void createGame() {
    }

    private void join() {
    }
    private void setColorMessage() {
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
        MessageView msg = new SetColorMessage(color, this.ID);
        updateClient(msg);
    }

    private void playCard(){

    }
    private void drawCard(){

    }
    private void writeInChat(){

    }
    private void rejoin(){

    }
    private void quit(){

    }

}

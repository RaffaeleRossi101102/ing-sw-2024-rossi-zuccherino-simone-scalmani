package SoftEng_2024.View;


import java.rmi.ServerError;
import java.util.Scanner;

public class CliViewClient {
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

    private void initializePlayer() {
    }

    private void createGame() {
    }

    private void join() {
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

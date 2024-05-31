/*package SoftEng_2024;

import SoftEng_2024.Controller.GameInit;
import SoftEng_2024.Model.*;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Player_and_Board.Cell;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;
import SoftEng_2024.Model.Fronts.ResourceFront;
import SoftEng_2024.Model.Player_and_Board.Player;

import java.rmi.RemoteException;
import java.util.*;

public class Simulator {
    //riempiamo i player
    static GameInit controller;

    public static void main(String[] args) throws Board.notAvailableCellException, Board.necessaryResourcesNotAvailable, RemoteException {
        //creazione carta che fa vincere
        Angles[] frontAngles1= {Angles.EMPTY,Angles.EMPTY,Angles.EMPTY, Angles.EMPTY};
        Angles[] resources= {Angles.EMPTY,Angles.EMPTY,Angles.EMPTY,Angles.EMPTY,Angles.INSECTS};
        boolean[] covered= {false,false,false,false};
        Front resourceFront1= (ResourceFront) new ResourceFront(frontAngles1,20,covered);
        Card resourceCard1= (ResourceCard)  new ResourceCard(resourceFront1,false,resources);
        //istanziazione dei players. Sono vuoti. Simula il collegamento dei client
        List<Player> players = new ArrayList<>();
        Cell[][] matrix4 = new Cell[85][85];
        Cell[][] matrix1 = new Cell[85][85];
        Cell[][] matrix2 = new Cell[85][85];
        Cell[][] matrix3 = new Cell[85][85];
        for(int i=0; i< matrix1.length; i++) {
            // System.out.println(i);
            for(int j=0;j<matrix1[0].length;j++){
                //   System.out.println(j);
                matrix1[i][j]= new Cell();
            }
        }
        for(int i=0; i< matrix1.length; i++) {
            // System.out.println(i);
            for(int j=0;j<matrix1[0].length;j++){
                //   System.out.println(j);
                matrix2[i][j]= new Cell();
            }
        }
        for(int i=0; i< matrix1.length; i++) {
            // System.out.println(i);
            for(int j=0;j<matrix1[0].length;j++){
                //   System.out.println(j);
                matrix3[i][j]= new Cell();
            }
        }
        for(int i=0; i< matrix1.length; i++) {
            // System.out.println(i);
            for(int j=0;j<matrix1[0].length;j++){
                //   System.out.println(j);
                matrix4[i][j]= new Cell();
            }
        }
        Player player1 = new Player(new ArrayList<Card>(), new Board());
        players.add(player1);
        Player player2 = new Player(new ArrayList<Card>(), new Board( ));
        players.add(player2);
        Player player3 = new Player(new ArrayList<Card>(), new Board( ));
        players.add(player3);
        Player player4 = new Player(new ArrayList<Card>(), new Board( ));
        players.add(player4);
        //chiamata a game init che istanzia la classe game e le passa tutti i deck con i player
        //abbiamo creato tutte le carte di gioco e le abbiamo assegnate al Game.
        // Pure le public cards e le starterCards sono assegnate
        controller = new GameInit();
        controller.gameInit();
        controller.getGame().getPlayers().addAll(players);
        for(Player player: controller.getGame().getPlayers()){
            //ad ogni player viene aggiunta una carta dello starter deck
            player.setHand(controller.getGame().getStarterDeck().poll());
        }

        Scanner scan5= new Scanner(System.in);
        System.out.println("Inserisci i nickname:");
        for(Player pl:players) {
            String nickname = scan5.nextLine();
            pl.setNickname(nickname);
        }
        int n=0;
        for(Player pl:players) {
            System.out.println("Starter di "+pl.getNickname()+":");
            Card card = pl.getHand().get(0);
            System.out.print("Front: [");
            for (int j = 0; j < 7; j++) {
                System.out.print( card.getFront().getFrontAngles()[j]+"-");
            }
            System.out.println("]");
            System.out.print("Back: [");
            for (int j = 0; j < 4; j++) {
                System.out.print( card.getResources()[j]+"-");
            }
            System.out.println("]");
            n++;
        }
        //Ora, ogni player dovrà decidere in che verso piazzare la carta starter
        Scanner scan= new Scanner(System.in);
        System.out.println("Inserire true o false per ogni player :");
        for(Player pl:players) {
            if(scan.nextBoolean()){
                controller.playStarterCard(true,pl);
            }
            else controller.playStarterCard(false,pl);
        }
        controller.updatePlayerHands();
        controller.updatePublicGoals();
        for(Player pl:players) {
            controller.privateGoal(controller.getGame().getGoalCardDeck().poll(), pl);
        }
        boolean endMatch = false;
        while(!endMatch){
            for(Player pl:players){
                controller.getGame().turnStart();
                // SHOWING THE PERSONAL CARD TO THE CURRENT PLAYER
                for(int i=0; i<3; i++) {
                    Card currentCard = controller.getGame().getCurrentPlayer().getHand().get(i);
                    System.out.print("TIPO: [" + currentCard.getResources()[4]+"]   ");
                    System.out.print("PUNTI: [" + currentCard.getFront().getPoints()+"] ");
                    System.out.print("ANGOLI: [");
                    for (int j = 0; j < 4; j++) {
                        System.out.print( (j + 1)+": " + currentCard.getFront().getFrontAngles()[j] +"- ");
                    }
                    System.out.println("]");
                    if (currentCard.getClass().getName().equals("SoftEng_2024.Cards.GoldCard")) {
                        System.out.println("SCORETYPES:" + currentCard.getFront().getScoreTypes());
                        System.out.print("REQ.RES. [");
                        for (int j = 0; j < 5; j++) {
                            System.out.print( ""+(j + 1)+" " + currentCard.getFront().getRequiredResources()[j]+"- ");
                        }
                        System.out.println("]");
                    }
                }
                // CURRENT PLAYER CHOOSE THE CARD THAT WANTS PLAY FROM HIS HAND'S CARDS
                Scanner scan2 = new Scanner(System.in);
                System.out.println(controller.getGame().getCurrentPlayer().getNickname() + " é il tuo turno, inserisci la carta [0-2], dove pizzarla [r-c] e come true/false:");
                int indice1 = scan2.nextInt();
                int indice2 = scan2.nextInt();
                int indice3 = scan2.nextInt();
                boolean flip = scan2.nextBoolean();
                System.out.println(indice1+"-" + indice2+"-" + indice3+"-" + "bool: "+ flip);
                controller.getGame().getCurrentPlayer().getHand().get(indice1).setFlipped(flip);
                controller.getGame().playCard(controller.getGame().getCurrentPlayer().getHand().get(indice1),controller.getGame().getCurrentPlayer(), indice2, indice3);
                //SHOWING THE PUBLIC CARDS
                System.out.println("CARTE DA CUI PESCARE : ");
                for(int i=0; i<4; i++){
                    Card card = controller.getGame().getPublicCards().get(i);
                    System.out.print("TIPO: [" + card.getResources()[4]+"]  ");
                    System.out.print("PUNTI:[" + card.getFront().getPoints()+"]   ");
                    System.out.print("Angoli: [");
                    for (int j = 0; j < 4; j++) {
                        System.out.print( (j + 1)+": " + card.getFront().getFrontAngles()[j]+"- ");
                    }
                    System.out.println("]");
                    if (card.getClass().getName().equals("SoftEng_2024.Cards.GoldCard")) {
                        System.out.println("SCORETYPES:" + card.getFront().getScoreTypes());
                        System.out.print("REQ.RES. [");
                        for (int j = 0; j < 5; j++) {
                            System.out.print("" + (j + 1)+" " + card.getFront().getRequiredResources()[j]+"- ");
                        }
                        System.out.println("]");
                    }

                }
                // CURRENT PLAYER CHOOSE IF HE WANTS TO DRAW FROM DECK OR FROM PUBLIC CARDS
                Scanner scan3 = new Scanner(System.in);
                System.out.println("PESCA DAL MAZZO-> 1 oppure PESCA DA TERRA-> 2 OPPURE VINCI-> 5. SE MAZZO DECIDI MAZZO 0/1, SE TERRA 0/3");
                int deck = scan3.nextInt();
                int position = scan3.nextInt();
                if(deck==1){
                    controller.getGame().drawFromTheDeck(controller.getGame().getCurrentPlayer(), position);
                }else if(deck==2){
                    controller.getGame().drawPublicCards(controller.getGame().getCurrentPlayer(), position);
                }
                else if(deck==5)    controller.getGame().getCurrentPlayer().getPlayerBoard().setScore(20);
                endMatch = controller.getGame().turnEnd();
            }
        }
    }
}
*/
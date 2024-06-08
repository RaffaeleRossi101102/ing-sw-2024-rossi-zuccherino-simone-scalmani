package SoftEng_2024.View;


import SoftEng_2024.Model.Cards.*;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.GoalCard.GoalCard;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocalModel {
    private ConcurrentHashMap<String,List<Card>> otherPlayershand=new ConcurrentHashMap<>();
    private String nickname;
    private String currentTurnPlayerNickname;
    private List<Card> personalHand;
    private ConcurrentHashMap<String,Color> playersColor;
    private volatile StarterCard starterCard;
    private GameState gameState;
    private GameState playerState;
    private List<GoalCard> availableGoals;
    private List<String> winnersNickname;
    private volatile boolean ackReceived;
    private volatile boolean ackSuccessful;
    private List<String> playersNickname;
    private List<Card> publicCards;
    private volatile List<String> errorLog= new ArrayList<>();
    private ConcurrentLinkedDeque<String> chat = new ConcurrentLinkedDeque<>();
    //GETTERS******************************************************************
    public GameState getState(){return gameState;}

    public ConcurrentHashMap<String, List<Card>> getOtherPlayershand() {
        return otherPlayershand;
    }


    public List<Card> getPublicCards() {
        return publicCards;
    }

    public GameState getPlayerState(){
        return this.playerState;
    }

    public List<GoalCard> getAvailableGoals() {
        return availableGoals;
    }

    public StarterCard getStarterCard() {
        return starterCard;
    }

    public ConcurrentHashMap<String, Color> getPlayersColor() {
        return playersColor;
    }

    public List<Card> getPersonalHand() {
        return personalHand;
    }

    public String getCurrentTurnPlayerNickname() {
        return currentTurnPlayerNickname;
    }

    public String getNickname() {return nickname;}

    public List<String> getPlayersNickname() {return playersNickname;}

    public List<String> getWinnersNickname() {return winnersNickname;}

    public boolean isAckReceived() {return ackReceived;}

    public boolean isAckSuccessful() {
        return ackSuccessful;
    }

    public List<String> getErrorLog() {return errorLog;};

    public Queue<String> getChat() {return chat;}

    public List<String> getLastNMessages(int n){
        List<String> subQueue = new CopyOnWriteArrayList<>();
        Iterator<String> it = chat.descendingIterator();

        int count = 0;
        while(it.hasNext() && count < n){
            subQueue.add(it.next());
            count++;
        }

        return subQueue;
    }

    //SETTERS******************************************************************
    public void setOtherPlayersHand(String playerNickname, List<Card> otherPlayersHand) {
        this.otherPlayershand.replace(playerNickname,otherPlayersHand);
    }

    public void setPublicCards(List<Card> publicCards) {
        this.publicCards = publicCards;
        System.out.println("Got some public cards which are: ");
        for (Card card:this.publicCards){
            System.out.println(card);
        }
    }
    public void testMessage(List<Integer> test){
        System.out.println("Lista arrivata");
        for(Integer i:test){
            System.out.print(i+" ");
        }
    }
    public void setPersonalHand(List<Card> personalHand) {
        this.personalHand = personalHand;
    }

    public void setAvailableGoals(List<GoalCard> availableGoals) {
        this.availableGoals = availableGoals;
    }

    public void setPlayersColor(String nickname,Color color) {
        this.playersColor.put(nickname,color);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        System.out.println("Settando il gameState a:" + this.gameState);
    }

    public void setPlayerState(GameState playerState) {
        this.playerState = playerState;
        System.out.println("settando il player state a "+playerState);
    }

    public void setStarterCard(StarterCard starterCard) {
        System.out.println("sto settando la starter ooooo "+ this.starterCard);
        this.starterCard = starterCard;
    }

    public void setCurrentTurnPlayerNickname(String currentTurnPlayerNickname) {
        this.currentTurnPlayerNickname = currentTurnPlayerNickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        System.out.println("set my nick");
    }

    public void setPlayersNickname(String playersNickname) {
        System.out.println("set other's nick");
        this.playersNickname.add(playersNickname);
        otherPlayershand.put(nickname,new ArrayList<>());
        playersColor.put(nickname,null);
    }

    public void setWinnersNickname(List<String> winnersNickname) {this.winnersNickname = winnersNickname;}


    public void setAckReceived(boolean ackReceived) {this.ackReceived = ackReceived;}

    public void setAckSuccessful(boolean ackSuccessful) {this.ackSuccessful = ackSuccessful;}

    public synchronized void setErrorLog(String error) {this.errorLog.add(error);}

    public void addToChat(String msg) {this.chat.add(msg);}

}

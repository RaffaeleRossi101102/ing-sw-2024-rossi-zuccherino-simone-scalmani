package SoftEng_2024.View;


import SoftEng_2024.Model.Cards.*;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.Player_and_Board.Cell;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocalModel {
    private final ConcurrentHashMap<String,List<Card>> otherPlayersHand;
    private Angles topResourceCard;
    private Angles topGoldCard;
    private String nickname;
    private volatile String currentTurnPlayerNickname;
    private List<Card> personalHand;
    private final ConcurrentHashMap<String,Color> playersColor;
    private volatile StarterCard starterCard;
    private GameState gameState;
    private GameState playerState;
    private List<GoalCard> availableGoals;
    private List<String> winnersNickname;
    private volatile boolean ackReceived;
    private volatile boolean ackSuccessful;
    private final ConcurrentHashMap<String,Boolean> playersNickname;
    private List<Card> publicCards;
    private volatile String errorLog;
    private final ConcurrentLinkedDeque<String> chat;
    private final ConcurrentHashMap<String,LocalBoard> playersBoards;
    private volatile boolean allCardsArrived;
    private volatile boolean currentPlayerNotNull;
    private boolean isFirstPlayer;
    private final List<GoalCard> publicGoals;
    private volatile int numberOfMessages;
    private volatile int arrivedMessages;
    private String chatError;

    public LocalModel() {
        otherPlayersHand =new ConcurrentHashMap<>();
        personalHand= new ArrayList<>();
        playersColor= new ConcurrentHashMap<>();
        availableGoals=new ArrayList<>();
        winnersNickname= new ArrayList<>();
        playersNickname= new ConcurrentHashMap<>();
        publicCards= new ArrayList<>();
        chat = new ConcurrentLinkedDeque<>();
        playersBoards=new ConcurrentHashMap<>();
        this.allCardsArrived=false;
        this.currentPlayerNotNull=false;
        this.isFirstPlayer=false;
        this.publicGoals=new ArrayList<>();
        numberOfMessages=0;
        arrivedMessages=0;
        errorLog="";
    }

    //GETTERS******************************************************************
    public GameState getState(){return gameState;}

    public ConcurrentHashMap<String, List<Card>> getOtherPlayersHand() {
        return otherPlayersHand;
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

    public ConcurrentHashMap<String,Boolean> getPlayersNickname() {return playersNickname;}

    public List<String> getWinnersNickname() {return winnersNickname;}

    public boolean isAckReceived() {return ackReceived;}

    public boolean isAckSuccessful() {
        return ackSuccessful;
    }

    public String getErrorLog() {return errorLog;}

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

    public String getChatError() {
        return chatError;
    }

    //SETTERS******************************************************************
    public void setOtherPlayersHand(String playerNickname, List<Card> otherPlayersHand) {
        //System.out.println("in set other players hand"+playerNickname);
        if(this.otherPlayersHand.containsKey(playerNickname))
            this.otherPlayersHand.replace(playerNickname,otherPlayersHand);
        else
            this.otherPlayersHand.put(playerNickname,otherPlayersHand);
    }

    public void setChatError(String chatError) {
        this.chatError = chatError;
    }

    public void setPublicCards(List<Card> publicCards) {
        this.publicCards = publicCards;
//        System.out.println("Got some public cards which are: ");
//        for (Card card:this.publicCards){
//            System.out.println(card);
//        }
    }
    public void testMessage(List<Integer> test){
        //System.out.println("Lista arrivata");
        for(Integer i:test){
            System.out.print(i+" ");
        }
    }
    public void setPersonalHand(List<Card> personalHand) {
        this.personalHand = personalHand;
//        System.out.println(personalHand.size());
    }

    public void setAvailableGoals(List<GoalCard> availableGoals) {
        this.availableGoals = availableGoals;
    }

    public void setPlayersColor(String nickname,Color color) {
        this.playersColor.put(nickname,color);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        //System.out.println("Settando il gameState a:" + this.gameState);
    }

    public void setPlayerState(GameState playerState) {
        this.playerState = playerState;
        //System.out.println("settando il player state a "+playerState);
    }

    public void setStarterCard(StarterCard starterCard) {
//        System.out.println("sto settando la starter ooooo "+ starterCard);
        this.starterCard = starterCard;
    }

    public void setCurrentTurnPlayerNickname(String currentTurnPlayerNickname) {
        if(this.currentTurnPlayerNickname==null){
           // System.out.println("the current player is " +currentTurnPlayerNickname);
            this.currentTurnPlayerNickname = currentTurnPlayerNickname;
            currentPlayerNotNull=true;
        }
        else{
            this.currentTurnPlayerNickname=currentTurnPlayerNickname;
        }

    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
//        System.out.println(nickname);
    }

    public synchronized void setPlayersNickname(String playersNickname) {

        this.playersNickname.put(playersNickname,true);
        if(!otherPlayersHand.containsKey(playersNickname))
            otherPlayersHand.put(playersNickname,new ArrayList<>());
//        if(!playersColor.containsKey(playersNickname))
//            playersColor.put(playersNickname, Color.EMPTY);
        if(!playersBoards.containsKey(playersNickname)){
            playersBoards.put(playersNickname,new LocalBoard());
        }
        System.out.println(playersNickname);
    }
    public void setIfPlayerOnline(String nickname,Boolean isOnline){
        this.playersNickname.replace(nickname,isOnline);
    }
    public void setLocalBoard(String nickname, Cell[][] board,ArrayList<Cell> cardList,int points,int[] anglesCounter){
        //System.out.println("setting local board");
        if(!playersBoards.containsKey(nickname)){
            LocalBoard localBoard= new LocalBoard();
            playersBoards.put(nickname,localBoard);
        }
        LocalBoard localBoard= playersBoards.get(nickname);
        localBoard.setCardBoard(board);
        localBoard.setCardList(cardList);
        localBoard.setScore(points);
        localBoard.setAnglesCounter(anglesCounter);
    }

    public void setWinnersNickname(List<String> winnersNickname) {this.winnersNickname = winnersNickname;}


    public void setAckReceived(boolean ackReceived) {this.ackReceived = ackReceived;}

    public void setAckSuccessful(boolean ackSuccessful) {this.ackSuccessful = ackSuccessful;}

    public synchronized void setErrorLog(String error) {
        this.errorLog = error;

    }

    public ConcurrentHashMap<String, LocalBoard> getPlayersBoards() {
        return playersBoards;
    }

    public synchronized void addToChat(String msg) {
        this.chat.add(msg);
        //System.out.println("aggiunto messaggio "+msg);
    }
    public boolean getAllCardsArrived(){
        return this.allCardsArrived;
    }
    public boolean getCurrentPlayerArrived(){
        return this.currentPlayerNotNull;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    public void setTopResourceCard(Angles topResourceCard) {
        System.out.println("setting resource");
        this.topResourceCard = topResourceCard;
    }

    public void setTopGoldCard(Angles topGoldCard) {
        System.out.println("setting gold");
        this.topGoldCard = topGoldCard;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Angles getTopGoldCard() {
        return topGoldCard;
    }

    public Angles getTopResourceCard() {
        return topResourceCard;
    }

    public List<GoalCard> getPublicGoals() {
        return publicGoals;
    }

    public void setPublicGoals(GoalCard[] publicGoals) {
        this.publicGoals.add(publicGoals[0]);
        this.publicGoals.add(publicGoals[1]);
    }

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
//        System.err.println("HO SETTATO IL NUMBER OF MESSAGES AOOOO: "+this.numberOfMessages+" "+numberOfMessages);
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public int getArrivedMessages() {
        return arrivedMessages;
    }
    public void increaseArrivedMessages(){
        this.arrivedMessages++;
//        System.err.println("increasing "+arrivedMessages);
    }
}

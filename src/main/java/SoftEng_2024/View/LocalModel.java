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
/**
 * Represents the local model that manages game state, player information, cards,
 * boards, and communication in a multiplayer game environment.
 */
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
    private boolean quitAll;
    private boolean lastManStanding;
    /**
     * Constructs a new LocalModel instance initializing all necessary fields.
     */
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
        quitAll=false;
        lastManStanding=false;
    }

    //GETTERS******************************************************************
    /**
     * Retrieves the last man standing status.
     *
     * @return {@code true} if the game is in last man standing mode, {@code false} otherwise.
     */
    public boolean getLastManStanding() {
        return lastManStanding;
    }
    /**
     * Retrieves the quit all status.
     *
     * @return {@code true} if quit all is initiated, {@code false} otherwise.
     */
    public boolean getQuitALL(){
        return quitAll;
    }
    /**
     * Retrieves the current game state.
     * @return The current game state.
     */
    public GameState getState(){return gameState;}
    /**
     * Retrieves the map of other players' hands.
     * @return The map of other players' hands.
     */
    public ConcurrentHashMap<String, List<Card>> getOtherPlayersHand() {
        return otherPlayersHand;
    }

    /**
     * Retrieves the list of public cards.
     * @return The list of public cards.
     */
    public List<Card> getPublicCards() {
        return publicCards;
    }
    /**
     * Retrieves the player state.
     * @return The player state.
     */
    public GameState getPlayerState(){
        return this.playerState;
    }
    /**
     * Retrieves the list of available goal cards.
     * @return The list of available goal cards.
     */
    public List<GoalCard> getAvailableGoals() {
        return availableGoals;
    }
    /**
     * Retrieves the starter card.
     * @return The starter card.
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }
    /**
     * Retrieves the map of players' colors.
     * @return The map of players' colors.
     */
    public ConcurrentHashMap<String, Color> getPlayersColor() {
        return playersColor;
    }
    /**
     * Retrieves the player's personal hand.
     * @return The player's personal hand.
     */
    public List<Card> getPersonalHand() {
        return personalHand;
    }
    /**
     * Retrieves the nickname of the current turn player.
     * @return The nickname of the current turn player.
     */
    public String getCurrentTurnPlayerNickname() {
        return currentTurnPlayerNickname;
    }
    /**
     * Retrieves the nickname associated with this local model.
     * @return The nickname.
     */
    public String getNickname() {return nickname;}
    /**
     * Retrieves the map of players' nicknames and their online status.
     * @return The map of players' nicknames and their online status.
     */
    public ConcurrentHashMap<String,Boolean> getPlayersNickname() {return playersNickname;}
    /**
     * Retrieves the list of winners' nicknames.
     * @return The list of winners' nicknames.
     */
    public List<String> getWinnersNickname() {return winnersNickname;}
    /**
     * Checks if an acknowledgement has been received.
     * @return false if an acknowledgement has been received, otherwise true.
     * This inversion of the return boolean value was done because every check on this value tests if it is false.
     * By adding a not operator to each check condition, we changed the value at the source to simplify the checks.
     */
    public boolean isAckReceived() {return ackReceived;}
    /**
     * Checks if an acknowledgement was successful.
     * @return True if the acknowledgement was successful, otherwise false.
     */
    public boolean isAckSuccessful() {
        return ackSuccessful;
    }
    /**
     * Retrieves the error log message.
     * @return The error log message.
     */
    public String getErrorLog() {return errorLog;}
    /**
     * Retrieves the chat queue.
     * @return The chat queue.
     */
    public Queue<String> getChat() {return chat;}
    /**
     * Retrieves the last N messages from the chat queue.
     * @param n The number of messages to retrieve.
     * @return The list containing the last N messages from the chat queue.
     */
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
    /**
     * Retrieves the chat error message.
     * @return The chat error message.
     */
    public String getChatError() {
        return chatError;
    }

    //SETTERS******************************************************************
    /**
     * Sets the quit all status.
     *
     * @param quitAll {@code true} to initiate quit all, {@code false} otherwise.
     */
    public void setQuitAll(boolean quitAll){
        this.quitAll = quitAll;
    }
    /**
     * Sets the last man standing status.
     *
     * @param lastManStanding {@code true} to enable last man standing mode, {@code false} otherwise.
     */
    public void setLastManStanding(boolean lastManStanding){
        this.lastManStanding = lastManStanding;
    }
    /**
     * Sets the other players' hand for a specific player.
     * @param playerNickname The nickname of the player.
     * @param otherPlayersHand The list of cards in the player's hand.
     */
    public void setOtherPlayersHand(String playerNickname, List<Card> otherPlayersHand) {
        //System.out.println("in set other players hand"+playerNickname);
        if(this.otherPlayersHand.containsKey(playerNickname))
            this.otherPlayersHand.replace(playerNickname,otherPlayersHand);
        else
            this.otherPlayersHand.put(playerNickname,otherPlayersHand);
    }
    /**
     * Sets the chat error message.
     * @param chatError The chat error message to set.
     */
    public void setChatError(String chatError) {
        this.chatError = chatError;
    }
    /**
     * Sets the list of public cards.
     * @param publicCards The list of public cards to set.
     */
    public void setPublicCards(List<Card> publicCards) {
        this.publicCards = publicCards;
    }
    public void testMessage(List<Integer> test){
        //System.out.println("Lista arrivata");
        for(Integer i:test){
            System.out.print(i+" ");
        }
    }
    /**
     * Sets the player's personal hand.
     * @param personalHand The player's personal hand to set.
     */
    public void setPersonalHand(List<Card> personalHand) {
        this.personalHand = personalHand;
//        System.out.println(personalHand.size());
    }
    /**
     * Sets the list of available goal cards.
     * @param availableGoals The list of available goal cards to set.
     */
    public void setAvailableGoals(List<GoalCard> availableGoals) {
        this.availableGoals = availableGoals;
    }
    /**
     * Sets the color of a player.
     * @param nickname The nickname of the player.
     * @param color The color to set for the player.
     */
    public void setPlayersColor(String nickname,Color color) {
        this.playersColor.put(nickname,color);
    }
    /**
     * Sets the game state.
     * @param gameState The game state to set.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        //System.out.println("Settando il gameState a:" + this.gameState);
    }
    /**
     * Sets the player state.
     * @param playerState The player state to set.
     */
    public void setPlayerState(GameState playerState) {
        this.playerState = playerState;
        //System.out.println("settando il player state a "+playerState);
    }
    /**
     * Sets the starter card.
     * @param starterCard The starter card to set.
     */
    public void setStarterCard(StarterCard starterCard) {
//        System.out.println("sto settando la starter ooooo "+ starterCard);
        this.starterCard = starterCard;
    }
    /**
     * Sets the nickname of the current turn player.
     * @param currentTurnPlayerNickname The nickname of the current turn player to set.
     */
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
    /**
     * Sets the nickname associated with this local model.
     * @param nickname The nickname to set.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
//        System.out.println(nickname);
    }
    /**
     * Sets a player's nickname and marks them as online.
     * @param playersNickname The nickname of the player to set.
     */
    public synchronized void setPlayersNickname(String playersNickname) {

        this.playersNickname.put(playersNickname,true);
        if(!otherPlayersHand.containsKey(playersNickname))
            otherPlayersHand.put(playersNickname,new ArrayList<>());

        if(!playersBoards.containsKey(playersNickname)){
            playersBoards.put(playersNickname,new LocalBoard());
        }
        System.out.println(playersNickname + "has joined the game!");
    }
    /**
     * Sets the online status of a player.
     * @param nickname The nickname of the player.
     * @param isOnline True if the player is online, otherwise false.
     */
    public void setIfPlayerOnline(String nickname,Boolean isOnline){
        this.playersNickname.replace(nickname,isOnline);
    }
    /**
     * Sets the local board for a player.
     * @param nickname The nickname of the player.
     * @param board The board configuration to set.
     * @param cardList The list of cards associated with the board.
     * @param points The score of the board.
     * @param anglesCounter The counter for angles on the board.
     */
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
    /**
     * Sets the list of winners' nicknames.
     * @param winnersNickname The list of winners' nicknames to set.
     */
    public void setWinnersNickname(List<String> winnersNickname) {this.winnersNickname = winnersNickname;}

    /**
     * Sets whether an acknowledgement has been received.
     * @param ackReceived True if an acknowledgement has been received, otherwise false.
     */
    public void setAckReceived(boolean ackReceived) {this.ackReceived = ackReceived;}
    /**
     * Sets whether an acknowledgement was successful.
     * @param ackSuccessful True if the acknowledgement was successful, otherwise false.
     */
    public void setAckSuccessful(boolean ackSuccessful) {this.ackSuccessful = ackSuccessful;}
    /**
     * Sets the error log message.
     * @param error The error log message to set.
     */
    public synchronized void setErrorLog(String error) {
        this.errorLog = error;

    }
    /**
     * Retrieves the map of players' boards.
     * @return The map of players' boards.
     */
    public ConcurrentHashMap<String, LocalBoard> getPlayersBoards() {
        return playersBoards;
    }
    /**
     * Adds a message to the chat queue.
     * @param msg The message to add to the chat queue.
     */
    public synchronized void addToChat(String msg) {
        this.chat.add(msg);
        //System.out.println("aggiunto messaggio "+msg);
    }
    /**
     * Checks if all cards have arrived.
     *
     * @return {@code true} if all cards have arrived; {@code false} otherwise.
     */
    public boolean getAllCardsArrived(){
        return this.allCardsArrived;
    }
    /**
     * Checks if the current player is not null.
     *
     * @return {@code true} if the current player is not null; {@code false} otherwise.
     */
    public boolean getCurrentPlayerArrived(){
        return this.currentPlayerNotNull;
    }
    /**
     * Sets whether the player is the first player.
     * @param firstPlayer True if the player is the first player, otherwise false.
     */
    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }
    /**
     * Sets the top resource card angle.
     * @param topResourceCard The top resource card angle to set.
     */
    public void setTopResourceCard(Angles topResourceCard) {
        this.topResourceCard = topResourceCard;
    }
    /**
     * Sets the top gold card angle.
     * @param topGoldCard The top gold card angle to set.
     */
    public void setTopGoldCard(Angles topGoldCard) {
        this.topGoldCard = topGoldCard;
    }
    /**
     * Retrieves the current game state.
     * @return The current game state.
     */
    public GameState getGameState() {
        return gameState;
    }
    /**
     * Retrieves the top gold card angle.
     * @return The top gold card angle.
     */
    public Angles getTopGoldCard() {
        return topGoldCard;
    }
    /**
     * Retrieves the top resource card angle.
     * @return The top resource card angle.
     */
    public Angles getTopResourceCard() {
        return topResourceCard;
    }
    /**
     * Retrieves the list of public goals.
     * @return The list of public goals.
     */
    public List<GoalCard> getPublicGoals() {
        return publicGoals;
    }
    /**
     * Sets the list of public goals.
     * @param publicGoals The array of public goals to set.
     */
    public void setPublicGoals(GoalCard[] publicGoals) {
        this.publicGoals.add(publicGoals[0]);
        this.publicGoals.add(publicGoals[1]);
    }
    /**
     * Sets the number of messages.
     * @param numberOfMessages The number of messages to set.
     */
    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
//        System.err.println("HO SETTATO IL NUMBER OF MESSAGES AOOOO: "+this.numberOfMessages+" "+numberOfMessages);
    }
    /**
     * Retrieves the number of messages.
     * @return The number of messages.
     */
    public int getNumberOfMessages() {
        return numberOfMessages;
    }
    /**
     * Retrieves the number of arrived messages.
     * @return The number of arrived messages.
     */
    public int getArrivedMessages() {
        return arrivedMessages;
    }
    /**
     * Increases the count of arrived messages.
     */
    public void increaseArrivedMessages(){
        this.arrivedMessages++;
//        System.err.println("increasing "+arrivedMessages);
    }
}

package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.Observers.PlayerObserver;

import java.util.ArrayList;
import java.util.List;
//RELATED PROBLEMS CON I CASI DI TEST
public class Player {

    private List<Card> hand;
    private String nickname;
    private Board playerBoard;
    public boolean isPlaying;
    private List<GoalCard> availableGoals;
    private List<Color> color;
    private boolean isOnline;
    private List<PlayerObserver> playerObservers;
    private GameState playerState;
    //METHODS

    //CONSTRUCTOR
    public Player(List<Card> hand, Board playerBoard){
        this.hand=hand;
        this.playerBoard=playerBoard;
        this.isOnline=true;
        this.availableGoals=new ArrayList<>();
        this.color=new ArrayList<>();
        this.playerState=GameState.CONNECTION;
        playerObservers=new ArrayList<>();
    }
    //OBSERVER METHODS
    public void addObserver(PlayerObserver o){
        playerObservers.add(o);
    }
    public void removeObserver(PlayerObserver o){playerObservers.remove(o);}

    //SETTERS
    public void setNickname(String nickname,double ID) {
        this.nickname = nickname;
        for(PlayerObserver o:playerObservers)
            o.updatedNickname(nickname,ID);
    }
    public void setColor(Color color) {
        this.color.add(color);
        for(PlayerObserver o:playerObservers)
            o.updatedPlayerColor(color,nickname);
    }
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        for(PlayerObserver o:playerObservers)
            o.updatedIsPlayerOnline(isOnline,nickname);
    }
    public synchronized void setHand(Card card) {
        this.hand.add(card);

        for(PlayerObserver o:playerObservers)
            o.updatedHand(hand,nickname);
    }
    public void removeCard(Card playedCard){
        this.hand.remove(playedCard);
        for(PlayerObserver o:playerObservers)
            o.updatedHand(hand,nickname);
    }
    public void setPlayerState(GameState playerState) {
        this.playerState = playerState;
        for(PlayerObserver o:playerObservers)
            o.updatedPlayerState(playerState,nickname);
    }

    public void setAvailableGoals(List<GoalCard> availableGoals) {
        this.availableGoals = availableGoals;
        for(PlayerObserver o:playerObservers)
            o.updatedAvailableGoals(availableGoals,nickname);

    }
    //GETTERS

    public String getNickname() {
        return nickname;
    }
    public Board getPlayerBoard() {
        return playerBoard;
    }
    public List<Card> getHand() {
        return hand;
    }
    public List<Color> getColor() {
        return color;
    }
    public boolean getIsOnline() {
        return isOnline;
    }
    public List<GoalCard> getAvailableGoals() {
        return availableGoals;
    }
    public GameState getPlayerState() {
        return playerState;
    }


}

package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.Observers.PlayerObserver;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Card> hand;
    private final String nickname;
    private Board playerBoard;
    public boolean isPlaying;
    private List<GoalCard> availableGoals;
    private List<Color> color;
    private boolean isOnline;
    private List<PlayerObserver> playerObservers;
    private GameState playerState;
    //METHODS

    //CONSTRUCTOR
    public Player(List<Card> hand, Board playerBoard, String nickname){
        this.nickname = nickname;
        this.hand=hand;
        this.playerBoard=playerBoard;
        this.isOnline=true;
        this.availableGoals=new ArrayList<>();
        this.color=new ArrayList<>();
        this.playerState=GameState.CONNECTION;
        playerObservers=new ArrayList<>();
    }
    public void addObserver(PlayerObserver o){
        playerObservers.add(o);
    }
    //SETTERS

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

    public void setHand(Card card) {
        this.hand.add(card);
        notifyAllObservers();
    }

    public void setColor(Color color) {
        this.color.add(color);
        notifyAllObservers();
    }

    public List<Color> getColor() {
        return color;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        notifyAllObservers();
    }

    public List<GoalCard> getAvailableGoals() {
        return availableGoals;
    }

    public GameState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(GameState playerState) {
        this.playerState = playerState;
        notifyAllObservers();
    }
    private void notifyAllObservers(){
        for(PlayerObserver o:playerObservers){
            o.lookForPlayerChanges(this);
        }
    }
}

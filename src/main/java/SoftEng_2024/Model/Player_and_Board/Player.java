package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.ModelMessages.UpdatedPlayerStateMessage;
import SoftEng_2024.Model.Observers.PlayerColorObserver;
import SoftEng_2024.Model.Observers.PlayerHandObserver;
import SoftEng_2024.Model.Observers.PlayerObserver;
import SoftEng_2024.Network.ToView.ObServerManager;

import java.util.ArrayList;
import java.util.List;

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
    private void notifyAllObservers(){
        for(PlayerObserver o:playerObservers){
            o.notify(this);
        }
    }
    public void addObserver(PlayerObserver o){
        playerObservers.add(o);
    }
    public void removeObserver(PlayerObserver o){playerObservers.remove(o);}
    public void setAllPlayerObservers(Player newPlayer, double viewID, ObServerManager toViewManager){
        newPlayer.addObserver(new PlayerHandObserver(toViewManager,viewID));
        newPlayer.addObserver(new PlayerColorObserver(toViewManager,viewID));
    }

    //SETTERS
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setColor(Color color) {
        this.color.add(color);
        notifyAllObservers();
    }
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        notifyAllObservers();
    }
    public void setHand(Card card) {
        this.hand.add(card);
        notifyAllObservers();
    }
    public void setPlayerState(GameState playerState) {
        this.playerState = playerState;
        notifyAllObservers();
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

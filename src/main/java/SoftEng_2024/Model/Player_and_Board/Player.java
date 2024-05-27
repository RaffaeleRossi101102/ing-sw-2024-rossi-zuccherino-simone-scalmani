package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.GoalCard.GoalCard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Player {
    private List<Card> hand;
    private final String nickname;
    private Board playerBoard;
    public boolean isPlaying;
    private List<GoalCard> availableGoals;
    private List<Color> color;
    private boolean isOnline;
    //METHODS

    //CONSTRUCTOR
    public Player(List<Card> hand, Board playerBoard, String nickname){
        this.nickname = nickname;
        this.hand=hand;
        this.playerBoard=playerBoard;
        this.isOnline=true;
        this.availableGoals=new ArrayList<>();
        this.color=new ArrayList<>();
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
    }

    public void setColor(Color color) {
        this.color.add(color);
    }

    public List<Color> getColor() {
        return color;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public List<GoalCard> getAvailableGoals() {
        return availableGoals;
    }
}

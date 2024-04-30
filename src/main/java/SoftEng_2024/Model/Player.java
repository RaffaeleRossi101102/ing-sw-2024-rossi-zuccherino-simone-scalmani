package SoftEng_2024.Model;

import SoftEng_2024.Model.Cards.Card;

import java.util.List;

public class Player {
    private List<Card> hand;
    private final String nickname;
    private Board playerBoard;
    public boolean isPlaying;
    private volatile GoalCard goal;
    private String color;
    private boolean isOnline;
    private boolean disconnectionResilience = false;
    //METHODS

    //CONSTRUCTOR
    public Player(List<Card> hand,Board playerBoard, String nickname){
        this.nickname = nickname;
        this.hand=hand;
        this.playerBoard=playerBoard;
        this.isOnline=true;
    }
    //SETTERS

    //public void setNickname(String nickname) {this.nickname = nickname;}

    //GETTERS

    public GoalCard getGoal() {
        return goal;
    }

    public String getNickname() {
        return nickname;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    //Il giocatore tramite la view ha già scelto la carta obiettivo che le è stata proposta dal controller
    //in questo metodo la inserisco semplicemente tra le sue carte
    public void setGoalCard(GoalCard goals){
        this.goal=goals;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(Card card) {
        this.hand.add(card);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean getDisconnectionResilience() {
        return disconnectionResilience;
    }

    public void setDisconnectionResilience(boolean disconnectionResilience) {
        this.disconnectionResilience = disconnectionResilience;
    }
}

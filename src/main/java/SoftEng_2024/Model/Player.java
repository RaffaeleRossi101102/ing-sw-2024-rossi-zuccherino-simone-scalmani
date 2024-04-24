package SoftEng_2024.Model;

import SoftEng_2024.Controller.Game;
import SoftEng_2024.Model.Cards.Card;

import java.util.List;

public class Player {
    private List<Card> hand;
    private String nickname;
    private Board playerBoard;
    private boolean isOnline;
    public boolean isPlaying;
    private GoalCard goal;

    //METHODS

    //CONSTRUCTOR
    public Player(List<Card> hand,Board playerBoard){
        this.hand=hand;
        this.playerBoard=playerBoard;
    }
    //SETTERS

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setOnline(boolean online){
        this.isOnline= online;
    }

    //GETTERS

    public GoalCard getGoal() {
        return goal;
    }

    public String getNickname() {
        return nickname;
    }
    public boolean getIsOnline(){
        return isOnline;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    //metodo che viene chiamato dopo che il client joina
    //aggiorna la lista del game e setta a true isOnline
    public void joinGame(Game game){
        this.isPlaying=true;
        game.getPlayers().add(this);
    }
    //opposto di joinGame
    public void quitGame(Game game){
        this.isPlaying=false;
        game.getPlayers().remove(this);
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
}

package SoftEng_2024.Model.GoalCard;

import SoftEng_2024.Model.Player_and_Board.Board;

import java.io.Serializable;

// Classe astratta che rappresenta le carte Goal
public abstract class GoalCard implements Serializable {
    private String goalType;
    private int points;
    private int cardID;

    // Metodo per il calcolo del punteggio degli obiettivi da implementare nelle classi figlio
    abstract public int calcScore(Board playerBoard);
    // Costruttore della classe
    public GoalCard(int points, String goaltype, int cardID){
        this.points=points;
        this.goalType = goaltype;
        this.cardID = cardID;
    }
    // Getter dell'attributo privato points
    public int getPoints() {
        return points;
    }
    public String getGoalType(){
        return goalType;
    }

    public int getCardID() {
        return cardID;
    }
}


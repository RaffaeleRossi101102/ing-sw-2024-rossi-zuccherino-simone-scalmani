package SoftEng_2024.Model;

import SoftEng_2024.Model.Board;

import java.io.Serializable;

// Classe astratta che rappresenta le carte Goal
public abstract class GoalCard implements Serializable {
    private String goalType;
    private int points;

    // Metodo per il calcolo del punteggio degli obiettivi da implementare nelle classi figlio
    abstract public int calcScore(Board playerBoard);
    // Costruttore della classe
    public GoalCard(int points, String goaltype){

        this.points=points;
        this.goalType = goaltype;
    }
    // Getter dell'attributo privato points
    public int getPoints() {
        return points;
    }
    public String getGoalType(){
        return goalType;
    };
}


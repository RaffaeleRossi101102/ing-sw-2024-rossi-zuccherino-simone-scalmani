package SoftEng_2024.Model;

import SoftEng_2024.Model.Board;

// Classe astratta che rappresenta le carte Goal
public abstract class GoalCard {
    private int points;

    // Metodo per il calcolo del punteggio degli obiettivi da implementare nelle classi figlio
    abstract public int calcScore(Board playerBoard);
    // Costruttore della classe
    public GoalCard(int points){
        this.points=points;
    }
    // Getter dell'attributo privato points
    public int getPoints() {
        return points;
    }
}


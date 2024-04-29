package SoftEng_2024.Model;

import SoftEng_2024.Model.Enums.Angles;

// Classe che rappresenta le carte con il Goal delle triplette di un solo tipo di risorsa
public class ResourceGoalCard extends GoalCard{
    // Attributo che identifica il tipo di risorsa nella tripletta del Goal
    private final Angles resources;
    // Costruttore della classe
    public ResourceGoalCard(Angles resources, int points, String goaltype){
        super(points, goaltype);
        this.resources=resources;
    }

    @Override
    public int calcScore(Board playerBoard) {
        int res;
        // Array di comodo utile per semplificare la scrittura del codice
        // Prendo l'array di tutte le risorse/oggetti presenti nella board del giocatore
        int[] resourceCounter = playerBoard.getAnglesCounter();
        // Uso .ordinal per trovare la posizione della risorsa nell'array e guardo quante risorse sono presenti
        // e salvo nel risultato
        res = resourceCounter[resources.ordinal()];
        // Divido (divisione intera con troncamento) per 3 in modo da trovare quante triplette di quella risorsa
        // sono presenti nella board del giocatore, moltiplico per i punti del Goal
        res = (res/3);
        res= res * this.getPoints();
        return res;
    }
}

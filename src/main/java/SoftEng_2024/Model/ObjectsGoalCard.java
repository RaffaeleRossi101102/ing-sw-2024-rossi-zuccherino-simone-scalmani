package SoftEng_2024.Model;

import SoftEng_2024.Model.Enums.Angles;

// Classe che rappresenta le carte con il Goal delle coppie di un tipo di oggetto e delle triplette di oggetti differenti
public class ObjectsGoalCard extends GoalCard{
    // Array di oggetti nel Goal
    private final Angles[] objects;
    // Costruttore
    public ObjectsGoalCard(Angles[] objects, int points){
        super(points);
        this.objects=objects;
    }

    @Override
    public int calcScore(Board playerBoard) {
        // Inizializzo il risultato a 0 per evitare aggiunta di punti in caso di errore nella chiamata o nel metodo
        int res = 0;
        // Array di comodo utile per semplificare la scrittura del codice
        // Prendo l'array di tutte le risorse/oggetti presenti nella board del giocatore
        int[] objectsCounter = playerBoard.getAnglesCounter();
        // Inizializzo temporaneamente la minima quantitá presente sulla Board del primo oggetto nel Goal
        int minQuantity = objectsCounter[objects[0].ordinal()];
        // Se gli oggetti nel Goal sono due si tratta del Goal che conta la quantitá di coppie di oggetti
        // identici presenti sulla Board del giocatore, procedimento analogo a ResourceGoalCard.calcScore()
        if (objects.length == 2){
            res = objectsCounter[objects[0].ordinal()];
            res = (res/2)*this.getPoints();
            return res;
        }
        // Altrimenti si tratta del Goal che conta la quantitá di triplette di oggetti differenti
        // presenti sulla Board del giocatore.
        // Trovo l'oggetto con la minima quantitá di presenze sulla Board e moltiplico per i punti del Goal
        else if (objects.length == 3) {
            for (Angles obj : objects) {
                if (objectsCounter[obj.ordinal()] < minQuantity){
                    minQuantity = objectsCounter[obj.ordinal()];
                }
            }
            res = minQuantity * this.getPoints();
            return res;
        }

        // In caso di chiamata del metodo con array di oggetti non compatibile con i casi descritti sopra
        // restituisco 0 punti in modo da non aggiungere punti al giocatore senza un'inizializzazione
        // compatibile.
        return res;
    }
}

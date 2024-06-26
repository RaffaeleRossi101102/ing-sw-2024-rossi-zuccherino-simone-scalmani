package SoftEng_2024.Model.GoalCard;

import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Enums.Angles;

// Classe che rappresenta le carte con il Goal delle coppie di un tipo di oggetto e delle triplette di oggetti differenti
/**
 * Represents goal cards that involve counting pairs or triplets of specific objects (Angles) on the player's board.
 * Extends the {@link GoalCard} abstract class.
 */
public class ObjectsGoalCard extends GoalCard {
    // Array di oggetti nel Goal
    private final Angles[] objects;
    // Costruttore
    /**
     * Constructs an ObjectsGoalCard with specified objects, points, goal type, and card ID.
     *
     * @param objects  The array of objects (Angles) involved in the goal.
     * @param points   The points awarded for achieving this goal.
     * @param goaltype The type of goal this card represents.
     * @param cardID   The unique identifier for this card.
     */
    public ObjectsGoalCard(Angles[] objects, int points, String goaltype, int cardID){
        super(points, goaltype, cardID);
        this.objects=objects;
    }

    /**
     * Calculates the score based on the number of pairs or triplets of specific objects (Angles) present on the player's board.
     * For pairs, it counts the number of complete pairs of the same object.
     * For triplets, it counts the number of complete triplets of different objects.
     *
     * @param playerBoard The player's board containing cards and cells.
     * @return The calculated score based on the specific criteria of the goal card.
     */
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

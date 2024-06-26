package SoftEng_2024.Model.GoalCard;

import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Enums.Angles;

// Classe che rappresenta le carte con il Goal delle triplette di un solo tipo di risorsa
/**
 * Represents goal cards that involve counting triplets of a specific type of resource (Angles) on the player's board.
 * Extends the {@link GoalCard} abstract class.
 */
public class ResourceGoalCard extends GoalCard {
    // Attributo che identifica il tipo di risorsa nella tripletta del Goal
    private final Angles resources;
    // Costruttore della classe
    /**
     * Constructs a ResourceGoalCard with a specified resource type, points, goal type, and card ID.
     *
     * @param resources The specific resource (Angles) involved in the goal.
     * @param points    The points awarded for achieving this goal.
     * @param goaltype  The type of goal this card represents.
     * @param cardID    The unique identifier for this card.
     */
    public ResourceGoalCard(Angles resources, int points, String goaltype, int cardID) {
        super(points, goaltype, cardID);
        this.resources = resources;
    }

    /**
     * Calculates the score based on the number of triplets of a specific type of resource (Angles) present on the player's board.
     * It counts how many triplets of the specified resource are present and multiplies by the points of the goal.
     *
     * @param playerBoard The player's board containing cards and cells.
     * @return The calculated score based on the specific criteria of the goal card.
     */
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

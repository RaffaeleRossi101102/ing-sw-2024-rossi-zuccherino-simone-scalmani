package SoftEng_2024.Model.GoalCard;

import SoftEng_2024.Model.Player_and_Board.Board;

import java.io.Serializable;


/**
 * Abstract class representing a goal card in the game.
 * Subclasses must implement the {@link #calcScore(Board)} method to calculate the score based on specific criteria.
 */
public abstract class GoalCard implements Serializable {
    private String goalType;
    private int points;
    private int cardID;


    /**
     * Constructs a GoalCard with specified points, goal type, and card ID.
     *
     * @param points   The points awarded for achieving this goal.
     * @param goaltype The type of goal this card represents.
     * @param cardID   The unique identifier for this card.
     */
    public GoalCard(int points, String goaltype, int cardID){
        this.points=points;
        this.goalType = goaltype;
        this.cardID = cardID;
    }
    // Getter dell'attributo privato points
    /**
     * Getter for the points attribute of the goal card.
     *
     * @return The points awarded for achieving this goal.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for the goalType attribute of the goal card.
     *
     * @return The type of goal this card represents.
     */
    public String getGoalType(){
        return goalType;
    }

    /**
     * Abstract method to be implemented by subclasses.
     * Calculates the score for achieving the goal based on the player's board state.
     *
     * @param playerBoard The player's board containing cards and cells.
     * @return The calculated score based on the specific criteria of the goal card.
     */
    abstract public int calcScore(Board playerBoard);

    /**
     * Getter for the cardID attribute of the goal card.
     *
     * @return The unique identifier for this card.
     */
    public int getCardID() {
        return cardID;
    }
}


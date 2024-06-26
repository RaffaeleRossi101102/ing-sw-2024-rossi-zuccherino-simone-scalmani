package SoftEng_2024.Model.Fronts;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Abstract class representing the front side of a card in the game.
 * <p>
 * Each front side contains:
 * - {@code frontAngles}: An array of angles or resources required or present on the front side.
 * - {@code points}: Points awarded when the card is placed.
 * - {@code covered}: An array indicating which angles/resources are covered.
 * - {@code hidden}: A boolean indicating if the front side is hidden.
 */
abstract public class Front implements Serializable {
    private final Angles[] frontAngles;
    private boolean[] covered;
    private final int points;
    private boolean hidden;

    //METHODS

    //CONSTRUCTORS
    /**
     * Constructor for initializing a Front object with specified front angles, points, and covered status.
     *
     * @param frontAngles An array of Angles representing the angles or resources on the front side.
     * @param points      The points awarded when the card with this front side is placed.
     * @param covered     An array indicating which angles/resources are covered.
     */
    public Front(Angles[] frontAngles, int points,boolean[] covered){
        this.frontAngles=frontAngles;
        this.points=points;
        this.covered=covered;
        this.hidden=false;
    }
    //checking requirements for resource cards (always playable)
    /**
     * Abstract method to check if the required resources for a card are available.
     *
     * @param resources An array of integers representing available resources.
     * @param played    The card being played.
     * @return {@code true} if the required resources are available; {@code false} otherwise.
     */
    abstract public boolean checkRequiredResources(int[] resources, Card played);

    //getting the points that the player scored by placing the card
    /**
     * Abstract method to update the scored points based on the placement of the card.
     *
     * @param anglesCounter An array containing counts of angles/resources.
     * @param coveredAngles The number of covered angles/resources.
     * @return The updated points scored by placing the card.
     */
    abstract public int updateScoredPoints(int[] anglesCounter, int coveredAngles);
    //GETTERS
    /**
     * Retrieves the front angles/resources of the card.
     *
     * @return An array of Angles representing the front angles/resources.
     */
    public Angles[] getFrontAngles() {
        return frontAngles;
    }

    /**
     * Retrieves the points awarded when the card is placed.
     *
     * @return The points awarded.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Retrieves the array indicating which angles/resources are covered.
     *
     * @return An array of booleans indicating covered angles/resources.
     */
    public boolean[] getCovered() {
        return covered;
    }

    /**
     * Abstract method to get the score types associated with the front side.
     *
     * @return The ScoreTypes enum representing scoring mechanisms.
     */
    abstract public ScoreTypes getScoreTypes();

    /**
     * Abstract method to get the required resources associated with the front side.
     *
     * @return An array of Angles representing required resources.
     */
    public abstract Angles[] getRequiredResources();

    /**
     * Hides the front angles by setting all elements in the frontAngles array to Angles.EMPTY.
     * Also sets the hidden flag to true.
     */
    public void hideFrontAngles(){
        Arrays.fill(frontAngles, Angles.EMPTY);
        this.hidden=true;
    }

    /**
     * Checks if the front side is hidden.
     *
     * @return {@code true} if the front side is hidden; {@code false} otherwise.
     */
    public boolean getHidden(){
        return this.hidden;
    }

    /**
     * Sets the hidden status of the front side.
     *
     * @param hidden {@code true} to hide the front side; {@code false} to reveal it.
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Sets the covered status of an angle/resource at a specific index.
     *
     * @param index   The index of the angle/resource.
     * @param covered {@code true} to mark the angle/resource as covered; {@code false} to mark it as uncovered.
     */
    public void setCovered(int index,boolean covered) {
        this.covered[index] = covered;
    }
}

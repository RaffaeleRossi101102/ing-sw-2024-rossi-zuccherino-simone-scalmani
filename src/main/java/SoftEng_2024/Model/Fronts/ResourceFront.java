package SoftEng_2024.Model.Fronts;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.*;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;

/**
 * Represents the front side of a resource card in the game.
 * <p>
 * Extends the {@link Front} class, providing basic functionality for resource cards:
 * - {@code frontAngles}: An array of Angles representing the angles or resources on the front side.
 * - {@code points}: The points awarded when the card with this front side is placed.
 * - {@code covered}: An array indicating which angles/resources are covered.
 */
public class ResourceFront extends Front{

    /**
     * Constructor to initialize a ResourceFront object with specified front angles, points, and covered status.
     *
     * @param frontAngles An array of Angles representing the angles or resources on the front side.
     * @param points      The points awarded when the card with this front side is placed.
     * @param covered     An array indicating which angles/resources are covered.
     */
    public ResourceFront(Angles[] frontAngles, int points, boolean[] covered) {
        super(frontAngles, points, covered);
    }

    /**
     * Retrieves the points associated with the resource card front.
     *
     * @return The points awarded when the card is placed.
     */
    @Override
    public int getPoints() {
        return super.getPoints();
    }

    /**
     * Retrieves the score types associated with the resource card front.
     * Since this is a resource card, it returns null as it doesn't have a specific score type.
     *
     * @return Always returns null for resource cards.
     */
    @Override
    public ScoreTypes getScoreTypes() {
        return null;
    }

    /**
     * Retrieves the required resources associated with the resource card front.
     * Since this is a resource card, it returns an empty array as it doesn't have required resources.
     *
     * @return An empty array of Angles for resource cards.
     */
    @Override
    public Angles[] getRequiredResources() {
        return new Angles[0];
    }

    /**
     * Retrieves the front angles associated with the resource card front.
     *
     * @return The array of Angles representing the front angles/resources.
     */
    @Override
    public Angles[] getFrontAngles() {
        return super.getFrontAngles();
    }

    /**
     * Checks if the required resources for the resource card are available on the player's board.
     * Since this is a resource card, it always returns true.
     *
     * @param boardResources An array of integers representing available resources on the board.
     * @param played         The card being played.
     * @return Always returns true for resource cards.
     */
    @Override
    public boolean checkRequiredResources(int[] boardResources, Card played) {
        return true;
    }

    /**
     * Updates the scored points based on the placement of the resource card.
     * Since this is a resource card, it simply returns the points associated with the card.
     *
     * @param anglesCounter An array containing counts of angles/resources.
     * @param coveredAngles The number of covered angles/resources.
     * @return The points awarded when the resource card is placed.
     */
    @Override
    public int updateScoredPoints(int[] anglesCounter, int coveredAngles){
        return getPoints();
    }

    //This method has to confront the board Resources with the required ones

}

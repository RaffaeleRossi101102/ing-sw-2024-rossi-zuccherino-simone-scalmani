package SoftEng_2024.Model.Fronts;


import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;

/**
 * Represents the front side of a gold card in the game.
 * <p>
 * Extends the {@link ResourceFront} class, adding additional properties:
 * - {@code scoreTypes}: Specifies the type of scoring mechanism for the gold card.
 * - {@code requiredResources}: An array of Angles representing the resources required by the gold card.
 */
public class GoldFront extends ResourceFront{
    private final ScoreTypes scoreTypes;
    private final Angles[] requiredResources;

    /**
     * Constructor to initialize a GoldFront object with specified front angles, points, covered status,
     * score types, and required resources.
     *
     * @param frontAngles      An array of Angles representing the angles or resources on the front side.
     * @param points           The points awarded when the card with this front side is placed.
     * @param covered          An array indicating which angles/resources are covered.
     * @param scoreTypes       The ScoreTypes enum specifying the scoring mechanism for the gold card.
     * @param requiredResources An array of Angles representing the required resources for the gold card.
     */
    public GoldFront(Angles[] frontAngles, int points,boolean[] covered, ScoreTypes scoreTypes,Angles[] requiredResources) {
        super(frontAngles, points,covered);
        this.scoreTypes=scoreTypes;
        this.requiredResources=requiredResources;
    }

    //METHODS
    //GETTERS

    /**
     * Retrieves the score types associated with the gold card front.
     *
     * @return The ScoreTypes enum representing the scoring mechanism.
     */
    public ScoreTypes getScoreTypes() {
        return scoreTypes;
    }

    /**
     * Retrieves the required resources associated with the gold card front.
     *
     * @return An array of Angles representing the required resources.
     */
    public Angles[] getRequiredResources() {
        return requiredResources;
    }

    //This method has to confront the board Resources with the required ones
    /**
     * Checks if the required resources for the gold card are available on the player's board.
     * If the card is flipped, it returns true.
     * Otherwise, it compares the required resources with the player's board resources.
     *
     * @param boardResources An array of integers representing available resources on the board.
     * @param played         The card being played.
     * @return {@code true} if the required resources are available; {@code false} otherwise.
     */
    @Override
    public boolean checkRequiredResources(int[] boardResources, Card played) {
        if (played.getFlipped()) return true;
        //initializing the array in which
        int[] reqRes= new int[7];
        int index=0;
        //for each resource update req res
        for(Angles res:requiredResources){
            index=Angles.getIndex(res);
            if(index<7) reqRes[index]++;
        }
        //now that it has filled req res with the requirements it compares it with what the player
        //actually has on the board
        //if the board doesn't have enough resources, it returns false and ends the method
        for(int i=0; i<7; i++){
            if(boardResources[i]<reqRes[i]) return false;
        }
        //if it gets out of the for loop without returning, it means that the player has enough resources
        //to play the card so the method returns true
        return true;
    }

    /**
     * Updates the scored points based on the placement of the gold card.
     * Depending on the score type, calculates the points differently:
     * - If {@code scoreTypes} is ANGLES, multiplies covered angles by points.
     * - If {@code scoreTypes} is not STATIC, multiplies the count of specific angles by points.
     * - If {@code scoreTypes} is STATIC, returns the points as is.
     *
     * @param anglesCounter  An array containing counts of angles/resources.
     * @param coveredAngles  The number of covered angles/resources.
     * @return The updated points scored by placing the gold card.
     */
    @Override
    public int updateScoredPoints(int[] anglesCounter, int coveredAngles){
        //if the gold card is an angle type, i have to multiply covered angles by the score
        if(scoreTypes.equals(ScoreTypes.ANGLES)){
            return (coveredAngles * getPoints());
        }
        //depending on the score type, which has to differ from STATIC, I get the index of the object and multiply
        if(ScoreTypes.getIndex(scoreTypes) < 7){
            return (anglesCounter[ScoreTypes.getIndex(scoreTypes)] * getPoints());
        }
        if(scoreTypes.equals(ScoreTypes.STATIC)){
            return getPoints();
        }
        return 0;
    }


}

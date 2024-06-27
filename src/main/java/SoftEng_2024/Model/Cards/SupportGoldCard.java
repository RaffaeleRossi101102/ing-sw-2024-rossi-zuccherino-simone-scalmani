package SoftEng_2024.Model.Cards;

/**
 * A support class for deserializing Gold Card data from JSON files.
 * This class contains the necessary fields to map the JSON data for Gold Cards.
 */
public class SupportGoldCard {
    /**
     * An array of strings representing the angles on the Gold Card.
     */
    public String[] angles;

    /**
     * A string representing the back resource of the Gold Card.
     */
    public String backResource;

    /**
     * An integer representing the points awarded by the Gold Card.
     */
    public int points;

    /**
     * A string representing the score type of the Gold Card.
     */
    public String scoreType;

    /**
     * An array of strings representing the required resources for the Gold Card.
     */
    public String[] requiredResources;

    /**
     * An integer representing the unique identifier for the Gold Card.
     */
    public int cardID;
}

package SoftEng_2024.Model.Cards;

/**
 * A support class for deserializing Resource Card data from JSON files.
 * This class contains the necessary fields to map the JSON data for Resource Cards.
 */
public class SupportResourceCard {
    /**
     * An array of strings representing the angles on the Resource Card.
     */
    String[] angles;

    /**
     * A string representing the back resource of the Resource Card.
     */
    String backResource;

    /**
     * An integer representing the points awarded by the Resource Card.
     */
    int points;

    /**
     * An integer representing the unique identifier for the Resource Card.
     */
    int cardID;
}


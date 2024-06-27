package SoftEng_2024.Model.Cards;

/**
 * A support class for deserializing Starter Card data from JSON files.
 * This class contains the necessary fields to map the JSON data for Starter Cards.
 */
public class SupportStarterCard {
    /**
     * An array of strings representing the angles on the Starter Card.
     */
    String[] angles;

    /**
     * An array of strings representing the starting back resources of the Starter Card.
     */
    String[] startingBackResources;

    /**
     * An integer representing the unique identifier for the Starter Card.
     */
    int cardID;
}

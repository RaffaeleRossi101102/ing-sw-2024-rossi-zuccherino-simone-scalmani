package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;
/**
 * Represents a Resource Card in the game. A Resource Card is a type of card that provides
 * points when placed and inherits functionality from the abstract Card class.
 */
public class ResourceCard extends Card{
    //CONSTRUCTOR

    /**
     * Constructs a ResourceCard with the specified front, flipped state, resources, and ID.
     *
     * @param front    the front of the card
     * @param flipped  whether the card is flipped
     * @param resource the resources on the card's angles and at the center on the back of the card
     * @param cardID   the unique identifier for the card
     */
    public ResourceCard(Front front, Boolean flipped, Angles[] resource, int cardID){
        super(front,flipped,resource, cardID);
    }
    //inheriting the superclass method

    /**
     * Inherits and returns the sum of resources from the superclass method.
     *
     * @return an array where each index represents the count of a specific resource or object
     */
    @Override
    public int[] getSumResources() {
        return super.getSumResources();
    }

    /**
     * Provides a printable string representation of the Resource Card.
     *
     * @return a string describing the Resource Card and the points it gives when placed
     */
    @Override
    public String getPrintableCardString( ) {
        String info="This is a resource card that gives ";
        info=info+this.getFront().getPoints();
        info=info+" points when placed";

        return info;
    }
}


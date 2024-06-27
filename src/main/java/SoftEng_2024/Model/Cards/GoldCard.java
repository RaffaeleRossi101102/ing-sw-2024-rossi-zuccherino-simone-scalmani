package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;
import SoftEng_2024.Model.Fronts.Front;

/**
 * Represents a Gold Card in the game, which is a type of Resource Card.
 * A Gold Card requires specific resources to be played and provides points.
 */
public class GoldCard extends ResourceCard {
    //CONSTRUCTOR
    /**
     * Constructs a GoldCard with the specified front, flipped state, resources, and ID.
     *
     * @param front    the front of the card
     * @param flipped  whether the card is flipped
     * @param resource the resources on the card's angles and at the center on the back of the card
     * @param cardID   the unique identifier for the card
     */
    public GoldCard(Front front, Boolean flipped, Angles[] resource,int cardID) {
        super(front, flipped, resource,cardID);
    }

    //overriding superclass' method

    /**
     * Provides a printable string representation of the Gold Card.
     *
     * @return a string describing the Gold Card, its required resources, and the points it gives
     */
    @Override
    public String getPrintableCardString() {
        String printableCardString = "This is a gold card which requires: [";
        for (int i = 0; i < 5; i++) {
            if (i != 4)
                printableCardString = printableCardString + Angles.getAngleSymbol(this.getFront().getRequiredResources()[i]) + "-";
            else
                printableCardString = printableCardString + Angles.getAngleSymbol(this.getFront().getRequiredResources()[i]) + "]";
        }
        printableCardString = printableCardString + " to be played and gives ";
        printableCardString = printableCardString + this.getFront().getPoints() + " points " + ScoreTypes.getScoreType(this.getFront().getScoreTypes());
        return printableCardString;
    }
}



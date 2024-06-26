package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;

import java.io.Serializable;

/**
 * Represents a Starter Card in the game. A Starter Card is a type of card that
 * can be played on its front or back at the start of the game.
 */
public class StarterCard extends Card {

    //private ResourceFront front;

    //CONSTRUCTOR
    /**
     * Constructs a StarterCard with the specified front, flipped state, resources, and ID.
     *
     * @param front    the front of the card
     * @param flipped  whether the card is flipped
     * @param resource the resources on the card's angles and at the center on the back of the card
     * @param cardID   the unique identifier for the card
     */
    public StarterCard(Front front, Boolean flipped, Angles[] resource, int cardID) {
        super(front, flipped, resource,cardID);
    }

    //overriding superclass' method

    /**
     * Overrides the superclass method to count the resources in the card's angles.
     * This implementation accounts for the specific behavior of a Starter Card.
     *
     * <p>The method initializes an array to hold the count of each type of resource or object.
     * If the card is played on its back (flipped), it counts the resources from the back angles.
     * If the card is played on its front, it counts the resources from the front angles.</p>
     *
     * @return an array where each index represents the count of a specific resource or object
     */
    @Override
    public int[] getSumResources() {
        //initializing the array that will be returned and the index variable
        int[] res = new int[7];
        int index;
        //if the card is played on its back:
        //the Angles correspond to the "central resources"
        if (getFlipped()) {
            //for each resource in the Card's resource array, find the index and
            //update res[]
            for (Angles ang : this.getCardBackAnglesType()) {
                //index contains the index of the specific resource in the back
                index = Angles.getIndex(ang);
                if(index<7) res[index]++;
            }
        }
        //if the card is played on its front
        else {
            for (Angles angle : this.getFront().getFrontAngles()) {
                //getting the resource's corresponding index
                index = Angles.getIndex(angle);
                //if the angle isn't empty, add the resource to the array
                if (index < 4) res[index]++;
                //if it's empty it checks the next angle
            }
        }
        return res;
    }

    /**
     * Provides a printable string representation of the Starter Card.
     * This method exists because StarterCard, inheriting from Card, must override this method.
     * However, it is not necessary for this implementation to return anything meaningful,
     * so it returns an empty string.
     *
     * @return an empty string
     */
    @Override
    public String getPrintableCardString(){
        return "";
    }

    /**
     * Displays the Starter Card in a graphical format as an array of strings.
     *
     * @return an array of strings representing the graphical display of the card
     */
    @Override
    public String[] displayGraphicCard() {
        String[] graphicCard = new String[3];
        char ULangle='█';
        char URangle='█';
        char DLangle='█';
        char DRangle='█';
        //char Cangle= Angles.getAngleSymbol(this.getCardBackAnglesType()[4]);
        if(this.getFlipped()){
            if (!this.getFront().getCovered()[0])
                ULangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[0]);
            if (!this.getFront().getCovered()[1])
                URangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[1]);
            if (!this.getFront().getCovered()[2])
                DLangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[2]);
            if (!this.getFront().getCovered()[3])
                DRangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[3]);


            graphicCard[0] = String.format("|%c-%c|", ULangle, URangle);
            graphicCard[1] = "|   |";
            graphicCard[2] = String.format("|%c-%c|", DLangle, DRangle);
        }
        else{
            char UCangle=Angles.getAngleSymbol(this.getFront().getFrontAngles()[4]);
            char MCangle=Angles.getAngleSymbol(this.getFront().getFrontAngles()[5]);
            char DCangle=Angles.getAngleSymbol(this.getFront().getFrontAngles()[6]);
            if (!this.getFront().getCovered()[0])
                ULangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[0]);
            if (!this.getFront().getCovered()[1])
                URangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[1]);
            if (!this.getFront().getCovered()[2])
                DLangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[2]);
            if (!this.getFront().getCovered()[3])
                DRangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[3]);
            graphicCard[0] = String.format("|%c%c%c|", ULangle,UCangle, URangle);
            graphicCard[1] = String.format("| %c |", MCangle);
            graphicCard[2] = String.format("|%c%c%c|", DLangle, DCangle, DRangle);


        }




        return graphicCard;
    }
}

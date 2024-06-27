package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;


import java.io.Serializable;
import java.util.Arrays;

/**
 * Abstract class representing a card in the game. A card has a front, can be flipped,
 * and contains resources represented by angles. Each card has a unique identifier.
 */
public abstract class Card implements Serializable {
    private Front front;
    private boolean flipped;
    private Angles[] resource;
    private int cardID;

    //METHODS
    /**
     * Constructs a Card with the specified front, flipped state, resources, and ID.
     *
     * @param front    the front of the card
     * @param flipped  whether the card is flipped
     * @param resource the resources on the angles and at the center on the back of the card
     * @param cardID   the unique identifier for the card
     */
    public Card(Front front, boolean flipped, Angles[] resource, int cardID) {
        this.front = front;
        this.flipped = flipped;
        this.resource = resource;
        this.cardID = cardID;
    }
    //GETTERS
    /**
     * Gets the unique identifier for the card.
     *
     * @return the card's unique identifier
     */
    public int getCardID(){
        return cardID;
    }

    /**
     * Gets the flipped state of the card.
     *
     * @return true if the card is flipped, false otherwise
     */
    public boolean getFlipped() {
        return flipped;
    }

    /**
     * Gets the front of the card.
     *
     * @return the front of the card
     */
    public Front getFront() {
        return front;
    }

    /**
     * Gets the resources on the back of the card.
     *
     * @return an array of angles representing the resources on the back of the card
     */
    public Angles[] getCardBackAnglesType(){
        return resource;
    }
    //Method that counts the number of resources or objects that are in the card's angles.
    // This specific implementation is for Resource and Object card. Starter cards will have to Override.
    /**
     * Counts the number of resources or objects in the card's angles.
     * This specific implementation is for Resource and Object cards.
     * Starter cards will have to override this method.
     *
     * @return an array where each index represents the count of a specific resource or object
     */
    public int[] getSumResources(){
        //initializing the array with all 0 values, since it still has to begin counting.
        //Its dimension is 7 since we have 4 resources and 3 objects
        int[] res = {0,0,0,0,0,0,0};
        int index = 0;
        //if the card is flipped
        if (flipped){
            // get the index of the central resource and update the counter
            index= Angles.getIndex(resource[4]);
            res[index]++;
        }
        else {
            //if the card isn't flipped
            //I need to access the angles and count the elements
            Angles[] angles = front.getFrontAngles();
            for (Angles angle : angles) {
                //If the angle is visible, I check wether it has resources, objects or if it's empty
                if (angle!=Angles.INVISIBLE) {
                    //checking if empty
                    if (angle==Angles.EMPTY) continue;

                        //else, there's a resource or an object, so it
                        //Gets the resource or object and translates it to the corresponding index
                    else index = Angles.getIndex(angle);
                    //once it knows the index in the array, it increments by 1 the value in that position
                    res[index]++;
                }
                //if the angle isn't visible, it skips to the next one
                else continue;

            }
            //when the loop ends, the array contains the number of the resources inside the card's angles,
            //and it's ready to return the array
        }
        return res;
    }

    /**
     * Sets the flipped state of the card.
     *
     * @param flipped the new flipped state
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    /**
     * Abstract method to get a printable string representation of the card.
     *
     * @return a string representation of the card
     */
    abstract public String getPrintableCardString();

    /**
     * Clones the resources on the back of the card.
     *
     * @return a cloned array of the resources on the back of the card
     */
    public Angles[] cloneBackResources(){
        Angles[] clone= new Angles[resource.length];
        System.arraycopy(resource, 0, clone, 0, resource.length);
        return clone;
    }

    /**
     * Displays the card in a graphical format as an array of strings.
     *
     * @return an array of strings representing the graphical display of the card
     */
    public String[] displayGraphicCard(){
        String[] graphicCard = new String[3];
        char ULangle='█';
        char URangle='█';
        char DLangle='█';
        char DRangle='█';
        char Cangle= Angles.getAngleSymbol(this.getCardBackAnglesType()[4]);
        if(!this.getFlipped()) {
            if (!this.getFront().getCovered()[0])
                ULangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[0]);
            if (!this.getFront().getCovered()[1])
                URangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[1]);
            if (!this.getFront().getCovered()[2])
                DLangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[2]);
            if (!this.getFront().getCovered()[3])
                DRangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[3]);
        }
        else{
            if (!this.getFront().getCovered()[0])
                ULangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[0]);
            if (!this.getFront().getCovered()[1])
                URangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[1]);
            if (!this.getFront().getCovered()[2])
                DLangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[2]);
            if (!this.getFront().getCovered()[3])
                DRangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[3]);
        }

        graphicCard[0] = String.format("|%c-%c|", ULangle, URangle);
        graphicCard[1] = String.format("| %c |", Cangle);
        graphicCard[2] = String.format("|%c-%c|", DLangle, DRangle);

        return graphicCard;
    }

}

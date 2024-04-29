package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;

public abstract class Card {
    private Front front;
    private Boolean flipped;
    private Angles[] resource;

    //METHODS
    public Card(Front front, Boolean flipped, Angles[] resource){
        this.front=front;
        //flipped can also be dFront froeclared as false
        this.flipped=flipped;
        this.resource=resource;
    }
    //GETTERS

    public Boolean getFlipped() {
        return flipped;
    }

    public Front getFront() {
        return front;
    }

    public Angles[] getResources(){
        return resource;
    }
    //Method that counts the number of resources or objects that are in the card's angles.
    // This specific implementation is for Resource and Object card. Starter cards will have to Override.
    public int[] getResource(){
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

    public void setFlipped(Boolean flipped) {
        this.flipped = flipped;
    }

    abstract public String getPrintableCardString( boolean flipped);
}

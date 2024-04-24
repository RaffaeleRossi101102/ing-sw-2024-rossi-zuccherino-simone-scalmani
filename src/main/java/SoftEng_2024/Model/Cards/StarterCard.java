package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;

public class StarterCard extends Card {

    //private ResourceFront front;

    //CONSTRUCTOR
    public StarterCard(Front front, Boolean flipped, Angles[] resource){
        super(front,flipped,resource);
    }

    //overriding superclass' method

    @Override
    public int[] getResource() {
        //initializing the array that will be returned and the index variable
        int res[] = new int[7];
        int index;
        //if the card is played on its back:
        //the Angles correspond to the "central resources"
        if (getFlipped()) {
            //for each resource in the Card's resource array, find the index and
            //update res[]
            for (Angles ang : this.getResources()) {
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
}

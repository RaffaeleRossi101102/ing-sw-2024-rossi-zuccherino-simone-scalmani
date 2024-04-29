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
        int[] res = new int[7];
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
    @Override
    public String getPrintableCardString(boolean flipped){
        String printableCardString;

        if (flipped){
            printableCardString = String.format("starter card on its back with these type of angles:\n" +
                            "%s %s %s %s \n" +
                            "and nothing in the center",
                    this.getResources()[0],
                    this.getResources()[1],
                    this.getResources()[2],
                    this.getResources()[3]);

        }else{
            printableCardString = String.format("starter card on its front with these type of angles:%s %s %s %s\n" +
                            "and these resources in the center:\n" +
                            "%s %s %s",
                    this.getFront().getFrontAngles()[0],
                    this.getFront().getFrontAngles()[1],
                    this.getFront().getFrontAngles()[2],
                    this.getFront().getFrontAngles()[3],
                    this.getFront().getFrontAngles()[4],
                    this.getFront().getFrontAngles()[5],
                    this.getFront().getFrontAngles()[6]);
        }




        return printableCardString;
    }
}

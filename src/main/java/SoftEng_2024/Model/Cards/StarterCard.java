package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;

import java.io.Serializable;

public class StarterCard extends Card {

    //private ResourceFront front;

    //CONSTRUCTOR
    public StarterCard(Front front, Boolean flipped, Angles[] resource, int cardID) {
        super(front, flipped, resource,cardID);
    }

    //overriding superclass' method

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
    @Override
    public String getPrintableCardString(){
        String printableCardString="";

//        if (flipped){
//            printableCardString = String.format("starter card on its back with these type of angles:\n" +
//                            "%s %s %s %s \n" +
//                            "and nothing in the center",
//                    this.getCardBackAnglesType()[0],
//                    this.getCardBackAnglesType()[1],
//                    this.getCardBackAnglesType()[2],
//                    this.getCardBackAnglesType()[3]);
//
//        }else{
//            printableCardString = String.format("starter card on its front with these type of angles:%s %s %s %s\n" +
//                            "and these resources in the center:\n" +
//                            "%s %s %s",
//                    this.getFront().getFrontAngles()[0],
//                    this.getFront().getFrontAngles()[1],
//                    this.getFront().getFrontAngles()[2],
//                    this.getFront().getFrontAngles()[3],
//                    this.getFront().getFrontAngles()[4],
//                    this.getFront().getFrontAngles()[5],
//                    this.getFront().getFrontAngles()[6]);
//        }
//
//


        return printableCardString;
    }

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

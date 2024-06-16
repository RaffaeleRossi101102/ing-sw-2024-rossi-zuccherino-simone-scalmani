package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;
import SoftEng_2024.Model.Fronts.Front;

public class GoldCard extends ResourceCard {
    //CONSTRUCTORS
    public GoldCard(Front front, Boolean flipped, Angles[] resource,int cardID) {
        super(front, flipped, resource,cardID);
    }

    //overriding superclass' method
    

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
//        int[] requiredResources = new int[4];
//        for(Angles angles : this.getFront().getRequiredResources()){
//            if(Angles.getIndex(angles)<4)requiredResources[Angles.getIndex(angles)]++;
//        }
//        if (flipped){
//            printableCardString = String.format("%s gold card on its back with empty visible angles, no required resources to place and a visible %s in the center",
//                    this.getCardBackAnglesType()[4], this.getCardBackAnglesType()[4]);
//        }else{
//            printableCardString = String.format("%s gold card on its front with these required resources: %s:%s %s:%s %s:%s %s:%s\n" +
//                            ",these type of angles: %s %s %s %s\n" +
//                            "and nothing in the center", this.getCardBackAnglesType()[4],
//                    requiredResources[0], Angles.values()[0],
//                    requiredResources[1], Angles.values()[1],
//                    requiredResources[2], Angles.values()[2],
//                    requiredResources[3], Angles.values()[3],
//                    this.getFront().getFrontAngles()[0],
//                    this.getFront().getFrontAngles()[1],
//                    this.getFront().getFrontAngles()[2],
//                    this.getFront().getFrontAngles()[3]);
//        }

//    @Override
//    public String displayGraphicCard(){
//
//    }


package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;

public class ResourceCard extends Card{
    //CONSTRUCTOR
    //The front in input has to actually be
    public ResourceCard(Front front, Boolean flipped, Angles[] resource, int cardID){
        super(front,flipped,resource, cardID);
    }
    //inheriting the superclass method

    @Override
    public int[] getSumResources() {
        return super.getSumResources();
    }

    @Override
    public String getPrintableCardString( ) {
        String info="This is a resource card that gives ";
        info=info+this.getFront().getPoints();
        info=info+" points when placed";

        return info;
    }
}
//    @Override
//    public String displayGraphicCard() {
//        String graphicCard;
//        char ULangle='█';
//        char URangle='█';
//        char DLangle='█';
//        char DRangle='█';
//        char Cangle= Angles.getAngleSymbol(this.getCardBackAnglesType()[4]);
//        if(!this.getFlipped()) {
//            if (!this.getFront().getCovered()[0])
//                ULangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[0]);
//            if (!this.getFront().getCovered()[1])
//                URangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[1]);
//            if (!this.getFront().getCovered()[2])
//                DLangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[2]);
//            if (!this.getFront().getCovered()[3])
//                DRangle = Angles.getAngleSymbol(this.getFront().getFrontAngles()[3]);
//        }
//        else{
//            if (!this.getFront().getCovered()[0])
//                ULangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[0]);
//            if (!this.getFront().getCovered()[1])
//                URangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[1]);
//            if (!this.getFront().getCovered()[2])
//                DLangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[2]);
//            if (!this.getFront().getCovered()[3])
//                DRangle = Angles.getAngleSymbol(this.getCardBackAnglesType()[3]);
//        }
//
//
//
//        graphicCard = String.format("|%s-%s|\n" +
//                                    "| %s |\n" +
//                                    "|%s-%s|", ULangle, URangle, Cangle, DLangle, DRangle);
//
//
//        return graphicCard;
//

        //|P A|
        //| x |
        //|- F|





    // █


//    @Override
//    public String[] printCard() {
//        return new String[]{
//            " _______ ",
//String.format("|%s     %s|",this.getFront().getFrontAngles()[0],this.getFront().getFrontAngles()[1]),
//            String.format("|       |"),
//String.format("|%s     %s|",this.getFront().getFrontAngles()[2],this.getFront().getFrontAngles()[3]),
//            " _______ "
//        };
//    }


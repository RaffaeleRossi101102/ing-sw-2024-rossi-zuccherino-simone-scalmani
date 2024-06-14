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
    public int[] getResource() {
        return super.getResource();
    }

    @Override
    public String getPrintableCardString( boolean flipped) {
        String printableCardString;

        if (flipped){
            printableCardString = String.format("%s resource card on its back with empty visible angles and a visible %s in the center",
                    this.getResources()[4], this.getResources()[4]);
        }else{
            printableCardString = String.format("%s resource card on its front with these type of angles: %s %s %s %s\n" +
                            "and nothing in the center", this.getResources()[4],
                    this.getFront().getFrontAngles()[0],
                    this.getFront().getFrontAngles()[1],
                    this.getFront().getFrontAngles()[2],
                    this.getFront().getFrontAngles()[3]);
        }
        return printableCardString;
    }

    @Override
    public String[] printCard() {
        return new String[]{
            " _______ ",
String.format("|%s     %s|",this.getFront().getFrontAngles()[0],this.getFront().getFrontAngles()[1]),
            String.format("|       |"),
String.format("|%s     %s|",this.getFront().getFrontAngles()[2],this.getFront().getFrontAngles()[3]),
            " _______ "
        };
    }
}

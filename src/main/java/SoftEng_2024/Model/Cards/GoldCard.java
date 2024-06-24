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



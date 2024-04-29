package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;

public class GoldCard extends ResourceCard{
    //CONSTRUCTORS
    public GoldCard(Front front, Boolean flipped, Angles[] resource){
        super(front,flipped,resource);
    }
    //overriding superclass' method
    @Override
    public int[] getResource() {
        return super.getResource();
    }

    @Override
    public String getPrintableCardString( boolean flipped){
        String printableCardString;
        int[] requiredResources = new int[7];
        for(Angles angles : this.getFront().getRequiredResources()){
            if(angles.ordinal()<6)  requiredResources[angles.ordinal()]++;
        }
        if (flipped){
            printableCardString = String.format("%s gold card on its back with empty visible angles, no required resources to place and a visible %s in the center",
                    this.getResources()[4], this.getResources()[4]);
        }else{
            printableCardString = String.format("%s gold card on its front with these required resources: %s:%s %s:%s %s:%s %s:%s\n" +
                            ",these type of angles: %s %s %s %s\n" +
                            "and nothing in the center", this.getResources()[4],
                    requiredResources[0], Angles.values()[0],
                    requiredResources[1], Angles.values()[1],
                    requiredResources[2], Angles.values()[2],
                    requiredResources[3], Angles.values()[3],
                    this.getFront().getFrontAngles()[0],
                    this.getFront().getFrontAngles()[1],
                    this.getFront().getFrontAngles()[2],
                    this.getFront().getFrontAngles()[3]);
        }
        return printableCardString;
    }
}

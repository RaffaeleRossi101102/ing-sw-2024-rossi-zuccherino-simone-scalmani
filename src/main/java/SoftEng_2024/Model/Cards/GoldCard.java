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
}

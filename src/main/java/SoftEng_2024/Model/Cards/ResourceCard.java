package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;

public class ResourceCard extends Card{
    //CONSTRUCTOR
    //The front in input has to actually be
    public ResourceCard(Front front, Boolean flipped, Angles[] resource){
        super(front,flipped,resource);
    }
    //inheriting the superclass method

    @Override
    public int[] getResource() {
        return super.getResource();
    }
}

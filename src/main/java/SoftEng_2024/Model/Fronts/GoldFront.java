package SoftEng_2024.Model.Fronts;


import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;

public class GoldFront extends ResourceFront{
    private final ScoreTypes scoreTypes;
    private final Angles[] requiredResources;

    public GoldFront(Angles[] frontAngles, int points,boolean[] covered, ScoreTypes scoreTypes,Angles[] requiredResources) {
        super(frontAngles, points,covered);
        this.scoreTypes=scoreTypes;
        this.requiredResources=requiredResources;
    }

    //METHODS
    //GETTERS

    public ScoreTypes getScoreTypes() {
        return scoreTypes;
    }

    public Angles[] getRequiredResources() {
        return requiredResources;
    }

    //This method has to confront the board Resources with the required ones
    @Override
    public boolean checkRequiredResources(int[] boardResources) {
        //initializing the array in which
        int[] reqRes= new int[7];
        int index=0;
        //for each resource updates req res
        for(Angles res:requiredResources){
            index=Angles.getIndex(res);
            if(index<7) reqRes[index]++;
        }
        //now that it has filled req res with the requirements it compares it with what the player
        //actually has on the board
        //if the board doesn't have enough resources, it returns false and ends the method
        for(int i:boardResources){
            if(boardResources[i]<reqRes[i]) return false;
        }
        //if it gets out of the for loop without returning, it means that the player has enough resources
        //to play the card so the method returns true
        return true;
    }
    @Override
    public int updateScoredPoints(int[] anglesCounter, int coveredAngles){
        //if the gold card is an angle type, i have to multiply covered angles by the score
        if(scoreTypes.equals(ScoreTypes.ANGLES)){
            return (coveredAngles * getPoints());
        }
        //depending on the score type, which has to differ from STATIC, I get the index of the object and multiply
        if(ScoreTypes.getIndex(scoreTypes) < 7){
            return (anglesCounter[ScoreTypes.getIndex(scoreTypes)] * getPoints());
        }
        if(scoreTypes.equals(ScoreTypes.STATIC)){
            return getPoints();
        }
        return 0;
    }


}

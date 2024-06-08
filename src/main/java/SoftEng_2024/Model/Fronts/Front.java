package SoftEng_2024.Model.Fronts;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;

import java.io.Serializable;
import java.util.Arrays;

abstract public class Front implements Serializable {
    private final Angles[] frontAngles;
    private boolean[] covered;
    private final int points;
    private boolean hidden;

    //METHODS

    //CONSTRUCTORS
    public Front(Angles[] frontAngles, int points,boolean[] covered){
        this.frontAngles=frontAngles;
        this.points=points;
        this.covered=covered;
        this.hidden=false;
    }
    //checking requirements for resource cards (always playable)
    abstract public boolean checkRequiredResources(int[] resources, Card played);

    //getting the points that the player scored by placing the card
    abstract public int updateScoredPoints(int[] anglesCounter, int coveredAngles);
    //GETTERS
    public Angles[] getFrontAngles() {
        return frontAngles;
    }

    public int getPoints() {
        return points;
    }

    public boolean[] getCovered() {
        return covered;
    }

    abstract public ScoreTypes getScoreTypes();

    public abstract Angles[] getRequiredResources();
    public void hideFrontAngles(){
        Arrays.fill(frontAngles, Angles.EMPTY);
        this.hidden=true;
    }
    public boolean getHidden(){
        return this.hidden;
    }
}

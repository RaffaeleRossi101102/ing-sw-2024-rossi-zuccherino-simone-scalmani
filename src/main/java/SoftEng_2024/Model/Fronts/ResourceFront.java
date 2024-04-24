package SoftEng_2024.Model.Fronts;

import SoftEng_2024.Model.Enums.*;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;

public class ResourceFront extends Front{

    public ResourceFront(Angles[] frontAngles, int points, boolean[] covered) {
        super(frontAngles, points, covered);
    }

    @Override
    public int getPoints() {
        return super.getPoints();
    }

    @Override
    public ScoreTypes getScoreTypes() {
        return null;
    }

    @Override
    public Angles[] getRequiredResources() {
        return new Angles[0];
    }

    @Override
    public Angles[] getFrontAngles() {
        return super.getFrontAngles();
    }

    @Override
    public boolean checkRequiredResources(int[] boardResources) {
        return true;
    }

    @Override
    public int updateScoredPoints(int[] anglesCounter, int coveredAngles){
        return getPoints();
    }

    //This method has to confront the board Resources with the required ones

}

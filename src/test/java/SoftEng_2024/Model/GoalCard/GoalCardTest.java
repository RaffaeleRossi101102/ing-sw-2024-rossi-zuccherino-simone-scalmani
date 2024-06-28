package SoftEng_2024.Model.GoalCard;

import SoftEng_2024.Model.Enums.Angles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalCardTest {

    private GoalCard testGoal;

    @Test
    void getPoints() {
        testGoal = new DiagonalGoalCard(Angles.FUNGI, 2, true, String.format("Get %s points for each ascending diagonal pattern of mushroom cards on the board", 2), 0);

        assertEquals(2, testGoal.getPoints());
    }

    @Test
    void getGoalType() {
        testGoal = new DiagonalGoalCard(Angles.FUNGI, 2, true, String.format("Get %s points for each ascending diagonal pattern of mushroom cards on the board", 2), 0);

        assertEquals("Get 2 points for each ascending diagonal pattern of mushroom cards on the board", testGoal.getGoalType());
    }

    @Test
    void calcScore() {
    }

    @Test
    void getCardID() {
        testGoal = new DiagonalGoalCard(Angles.FUNGI, 2, true, String.format("Get %s points for each ascending diagonal pattern of mushroom cards on the board", 2), 0);

        assertEquals(0, testGoal.getCardID());
    }
}
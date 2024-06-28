package SoftEng_2024.Model.Fronts;

import SoftEng_2024.Model.Cards.GoldCard;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldFrontTest {

    GoldFront testFront;
    GoldCard testCard;

    @Test
    void getScoreTypes() {
        testFront = new GoldFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false}, ScoreTypes.STATIC, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});

        assertEquals(ScoreTypes.STATIC, testFront.getScoreTypes());
    }

    @Test
    void getRequiredResources() {
        testFront = new GoldFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false}, ScoreTypes.STATIC, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        Angles[] expected = new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY};

        assertArrayEquals(expected, testFront.getRequiredResources());
    }

    @Test
    void checkRequiredResources() {
        testFront = new GoldFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false}, ScoreTypes.STATIC, new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        testCard = new GoldCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 18);
        int[] boardres = {0, 2, 0, 0, 0, 0, 0};

        assertTrue(testFront.checkRequiredResources(boardres, testCard));
        boardres[1] = 4;
        assertTrue(testFront.checkRequiredResources(boardres, testCard));
        testCard = new GoldCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 18);
        assertTrue(testFront.checkRequiredResources(boardres, testCard));
    }

    @Test
    void updateScoredPoints() {
        testFront = new GoldFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 1, new boolean[]{false, false, false, false}, ScoreTypes.STATIC, new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        int[] anglescounter = {1, 0, 0, 0, 0, 0, 0};

        assertEquals(1, testFront.updateScoredPoints(anglescounter, 0));
        testFront = new GoldFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 2, new boolean[]{false, false, false, false}, ScoreTypes.ANGLES, new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        assertEquals(4, testFront.updateScoredPoints(anglescounter, 2));
        testFront = new GoldFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 2, new boolean[]{false, false, false, false}, ScoreTypes.INK, new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        assertEquals(0, testFront.updateScoredPoints(anglescounter, 0));
    }
}
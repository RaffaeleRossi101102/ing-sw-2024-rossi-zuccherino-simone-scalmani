package SoftEng_2024.Model.Fronts;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Enums.Angles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceFrontTest {

    private Card testCard;
    private Front testFront;

    @Test
    void getPoints(){
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});

        assertEquals(0, testFront.getPoints());
    }

    @Test
    void getScoreTypes() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});

        assertNull(testFront.getScoreTypes());
    }

    @Test
    void getRequiredResources() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});

        assertNotNull(testFront.getRequiredResources());
    }

    @Test
    void getFrontAngles() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        Angles[] angles = new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY};

        assertArrayEquals(angles, testFront.getFrontAngles());
    }

    @Test
    void checkRequiredResources() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 18);
        int[] boardres = {0, 2, 0, 0, 0, 0, 0};

        assertEquals(true, testFront.checkRequiredResources(boardres, testCard));
    }

    @Test
    void updateScoredPoints() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 2, new boolean[]{false, false, false, false});
        int[] anglescounter = {1, 0, 0, 0, 0, 0, 0};

        assertEquals(2, testFront.updateScoredPoints(anglescounter, 2));
    }
}
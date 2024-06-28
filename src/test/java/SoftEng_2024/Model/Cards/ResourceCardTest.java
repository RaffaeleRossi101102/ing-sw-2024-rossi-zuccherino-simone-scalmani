package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;
import SoftEng_2024.Model.Fronts.ResourceFront;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {

    private Card testCard;
    private Front testFront;

    @Test
    void getSumResources() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.INVISIBLE, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 18);
        int[] sumResources = new int[7];

        sumResources = testCard.getSumResources();
        assertEquals(2, sumResources[1]);

        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 18);
        sumResources = testCard.getSumResources();
        assertEquals(1, sumResources[1]);
    }

    @Test
    void getPrintableCardString() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.INVISIBLE, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 18);

        assertEquals("This is a resource card that gives 0 points when placed", testCard.getPrintableCardString());
    }
}
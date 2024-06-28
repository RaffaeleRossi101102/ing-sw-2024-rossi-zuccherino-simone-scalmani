package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.ResourceFront;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StarterCardTest {

    ResourceFront testFront;
    StarterCard testCard;

    @Test
    void getSumResources() {
        testFront = new ResourceFront(new Angles[]{Angles.PLANTS, Angles.PLANTS, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new StarterCard(testFront, false, new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0);

        int[] sumResources;

        sumResources = testCard.getSumResources();
        assertEquals(2, sumResources[3]);

        testCard = new StarterCard(testFront, true, new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0);
        sumResources = testCard.getSumResources();
        assertEquals(2, sumResources[1]);
    }

    @Test
    void getPrintableCardString() {
        testFront = new ResourceFront(new Angles[]{Angles.PLANTS, Angles.PLANTS, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new StarterCard(testFront, false, new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0);

        assertEquals("", testCard.getPrintableCardString());
    }

    @Test
    void displayGraphicCard() {
        testFront = new ResourceFront(new Angles[]{Angles.PLANTS, Angles.PLANTS, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new StarterCard(testFront, false, new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0);

        assertEquals("[|PØP|, | Ø |, |ØØØ|]", Arrays.toString(testCard.displayGraphicCard()));
        testCard = new StarterCard(testFront, true, new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0);
        assertEquals("[|F-F|, |   |, |Ø-Ø|]", Arrays.toString(testCard.displayGraphicCard()));
    }
}
package SoftEng_2024.Model.Cards;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;
import SoftEng_2024.Model.Fronts.ResourceFront;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private GameController gameController;
    private Card testCard;
    private Front testFront;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
    }

    @Test
    void getCardID() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 18);

        assertEquals(18, testCard.getCardID());
    }

    @Test
    void getFlipped() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 18);

        assertEquals(false, testCard.getFlipped());
    }

    @Test
    void getFront() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 18);

        assertEquals(testFront, testCard.getFront());
    }

    @Test
    void getCardBackAnglesType() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.FUNGI, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 18);
        Angles[] backAngles = testCard.getCardBackAnglesType();

        assertEquals(backAngles, testCard.getCardBackAnglesType());
    }

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
    void setFlipped() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.INVISIBLE, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 18);
        testCard.setFlipped(true);

        assertTrue(testCard.getFlipped());
    }

    @Test
    void getPrintableCardString() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.INVISIBLE, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 18);

        assertEquals("This is a resource card that gives 0 points when placed", testCard.getPrintableCardString());
    }

    @Test
    void cloneBackResources() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.INVISIBLE, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 18);

        Angles[] cloneBackResources = testCard.cloneBackResources();
        assertEquals(testCard.getCardBackAnglesType()[4], cloneBackResources[4]);
    }

    @Test
    void displayGraphicCard() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.INVISIBLE, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 18);

        assertEquals("[|F-F|, | F |, |--Ø|]", Arrays.toString(testCard.displayGraphicCard()));
        testCard.setFlipped(true);
        assertEquals("[|Ø-Ø|, | F |, |Ø-Ø|]", Arrays.toString(testCard.displayGraphicCard()));

        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI, Angles.INVISIBLE, Angles.EMPTY}, 0, new boolean[]{false, false, false});
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 18);
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            testCard.displayGraphicCard();
        });

        assertNotNull(exception.getMessage());
    }
}
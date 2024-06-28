package SoftEng_2024.Model.Cards;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.ScoreTypes;
import SoftEng_2024.Model.Fronts.GoldFront;
import SoftEng_2024.Model.Fronts.ResourceFront;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    GoldFront testFront;
    GoldCard testCard;

    @Test
    void getPrintableCardString() {
        testFront = new GoldFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false}, ScoreTypes.STATIC, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY});
        testCard = new GoldCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 18);

        assertEquals("This is a gold card which requires: [Ø-Ø-Ø-Ø-Ø] to be played and gives 0 points when placed", testCard.getPrintableCardString());
    }
}
package SoftEng_2024.Model.Enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTypesTest {

    @Test
    void getIndex() {
        assertEquals(8, ScoreTypes.getIndex(ScoreTypes.STATIC));
        assertEquals(4, ScoreTypes.getIndex(ScoreTypes.INK));
        assertEquals(5, ScoreTypes.getIndex(ScoreTypes.SCROLL));
        assertEquals(6, ScoreTypes.getIndex(ScoreTypes.FEATHER));
        assertEquals(7, ScoreTypes.getIndex(ScoreTypes.ANGLES));
    }

    @Test
    void getScoreType() {
        assertEquals("when placed", ScoreTypes.getScoreType(ScoreTypes.STATIC));
        assertEquals("for each ink in your board", ScoreTypes.getScoreType(ScoreTypes.INK));
        assertEquals("for each scroll in your board", ScoreTypes.getScoreType(ScoreTypes.SCROLL));
        assertEquals("for each feather in your board", ScoreTypes.getScoreType(ScoreTypes.FEATHER));
        assertEquals("for each angle you'll cover with this card", ScoreTypes.getScoreType(ScoreTypes.ANGLES));
    }
}
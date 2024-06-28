package SoftEng_2024.Model.Enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AngleIndexesTest {

    @Test
    void getIndex() {
        assertEquals(0, AngleIndexes.getIndex(AngleIndexes.UPLEFT));
        assertEquals(1, AngleIndexes.getIndex(AngleIndexes.UPRIGHT));
        assertEquals(2, AngleIndexes.getIndex(AngleIndexes.DOWNLEFT));
        assertEquals(3, AngleIndexes.getIndex(AngleIndexes.DOWNRIGHT));
    }
}
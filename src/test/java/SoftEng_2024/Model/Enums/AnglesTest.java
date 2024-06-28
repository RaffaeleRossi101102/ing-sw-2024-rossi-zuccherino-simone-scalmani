package SoftEng_2024.Model.Enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnglesTest {

    @Test
    void getIndex() {
        assertEquals(0, Angles.getIndex(Angles.INSECTS));
        assertEquals(1, Angles.getIndex(Angles.FUNGI));
        assertEquals(2, Angles.getIndex(Angles.ANIMALS));
        assertEquals(3, Angles.getIndex(Angles.PLANTS));
        assertEquals(4, Angles.getIndex(Angles.INK));
        assertEquals(5, Angles.getIndex(Angles.SCROLL));
        assertEquals(6, Angles.getIndex(Angles.FEATHER));
        assertEquals(7, Angles.getIndex(Angles.EMPTY));
        assertEquals(8, Angles.getIndex(Angles.INVISIBLE));
    }

    @Test
    void getAngleSymbol() {
        assertEquals('I', Angles.getAngleSymbol(Angles.INSECTS));
        assertEquals('F', Angles.getAngleSymbol(Angles.FUNGI));
        assertEquals('A', Angles.getAngleSymbol(Angles.ANIMALS));
        assertEquals('P', Angles.getAngleSymbol(Angles.PLANTS));
        assertEquals('K', Angles.getAngleSymbol(Angles.INK));
        assertEquals('S', Angles.getAngleSymbol(Angles.SCROLL));
        assertEquals('H', Angles.getAngleSymbol(Angles.FEATHER));
        assertEquals('Ø', Angles.getAngleSymbol(Angles.EMPTY));
        assertEquals('-', Angles.getAngleSymbol(Angles.INVISIBLE));
    }

    @Test
    void values() {
    }

    @Test
    void valueOf() {
    }
}
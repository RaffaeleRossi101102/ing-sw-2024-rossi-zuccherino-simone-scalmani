package SoftEng_2024.Model.Enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    public void testEnumValues() {
        assertEquals(6, Color.values().length); // Checking the number of enum constants

        assertEquals(Color.RED, Color.valueOf("RED"));
        assertEquals(Color.BLUE, Color.valueOf("BLUE"));
        assertEquals(Color.GREEN, Color.valueOf("GREEN"));
        assertEquals(Color.YELLOW, Color.valueOf("YELLOW"));
        assertEquals(Color.BLACK, Color.valueOf("BLACK"));
        assertEquals(Color.EMPTY, Color.valueOf("EMPTY"));
    }
}
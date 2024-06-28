package SoftEng_2024.Model.Enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellStateTest {

    @Test
    public void testEnumValues() {
        assertEquals(3, CellState.values().length); // Checking the number of enum constants

        assertEquals(CellState.PLACEABLE, CellState.valueOf("PLACEABLE"));
        assertEquals(CellState.NOTPLACEABLE, CellState.valueOf("NOTPLACEABLE"));
        assertEquals(CellState.BANNED, CellState.valueOf("BANNED"));
    }

}
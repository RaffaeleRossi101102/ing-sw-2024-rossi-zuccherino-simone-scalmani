package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.CellState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    Cell cell = new Cell();
    Card card = null;
    int row = 0;
    int col = 0;
    boolean visited = false;

    @Test
    void getCard() {
        cell.setCard(card);
        assertEquals(card, cell.getCard());
    }

    @Test
    void getColumn() {
        cell.setColumn(col);
        assertEquals(col, cell.getColumn());
    }

    @Test
    void getRow() {
        cell.setRow(row);
        assertEquals(row, cell.getRow());
    }

    @Test
    void getCellState() {
        cell.setCellState(CellState.PLACEABLE);
        assertEquals(CellState.PLACEABLE, cell.getCellState());
    }

    @Test
    void setCard() {
        cell.setCard(card);
        assertEquals(card, cell.getCard());
    }

    @Test
    void setColumn() {
        cell.setColumn(col);
        assertEquals(col, cell.getColumn());
    }

    @Test
    void setCellState() {
        cell.setCellState(CellState.PLACEABLE);
        assertEquals(CellState.PLACEABLE, cell.getCellState());
    }

    @Test
    void setRow() {
        cell.setRow(row);
        assertEquals(row, cell.getRow());
    }

    @Test
    void getVisited() {
        cell.setVisited(visited);
        assertEquals(visited, cell.getVisited());
    }

    @Test
    void setVisited() {
        cell.setVisited(true);
        assertTrue(cell.getVisited());
    }
}
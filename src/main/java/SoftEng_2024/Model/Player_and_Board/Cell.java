package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.CellState;

import java.io.Serializable;
/**
 * Represents a single cell on the game board.
 */
public class Cell implements Serializable {

    private Card card=null;
    private CellState cellState;
    private int row;
    private int column;
    private boolean visited;
    //METHODS

    //CONSTRUCTOR
    /**
     * Constructs a cell with default properties.
     * By default, the cell state is set to NOTPLACEABLE and visited is set to false.
     */
    public Cell(){
        this.cellState = CellState.NOTPLACEABLE;
        this.visited=false;
    }

    //GETTERS
    /**
     * Retrieves the card placed on this cell.
     *
     * @return The card placed on the cell.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Retrieves the column index of this cell.
     *
     * @return The column index of the cell.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Retrieves the row index of this cell.
     *
     * @return The row index of the cell.
     */
    public int getRow() {
        return row;
    }

    /**
     * Retrieves the state of this cell.
     *
     * @return The state of the cell.
     */
    public CellState getCellState(){
        return cellState;
    }

    //SETTERS
    /**
     * Sets the card on this cell.
     *
     * @param card The card to set on the cell.
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Sets the column index of this cell.
     *
     * @param column The column index to set.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Sets the state of this cell.
     *
     * @param cellState The state to set for the cell.
     */
    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    /**
     * Sets the row index of this cell.
     *
     * @param row The row index to set.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Checks if the cell has been visited.
     *
     * @return true if the cell has been visited, false otherwise.
     */
    public boolean getVisited(){
        return this.visited;
    }

    /**
     * Sets the visited status of the cell.
     *
     * @param visited true to mark the cell as visited, false otherwise.
     */
    public void setVisited(boolean visited){
        this.visited = visited;
    }

}


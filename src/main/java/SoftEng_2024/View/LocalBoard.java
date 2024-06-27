package SoftEng_2024.View;

import SoftEng_2024.Model.Player_and_Board.Cell;

import java.util.ArrayList;
/**
 * Represents the local board containing cells, card lists, angles counters, and score.
 */
public class LocalBoard {
    private Cell[][] cardBoard;
    private ArrayList<Cell> cardList;
    private int[] anglesCounter;
    private int score;
    /**
     * Constructs a LocalBoard object initializing the card board, card list, angles counter, and score.
     */
    public LocalBoard(){
        this.cardBoard= new Cell[85][85];
        for(int i=0; i< this.cardBoard.length; i++){
            for(int j=0; j< this.cardBoard[0].length; j++){
                this.cardBoard[i][j]=new Cell();
            }
        }
        this.cardList = new ArrayList<>();
        this.anglesCounter = new int[7];
        this.score = 0;
    }
    /**
     * Retrieves the list of cells on the board.
     *
     * @return The list of cells on the board.
     */
    public ArrayList<Cell> getCardList() {
        return cardList;
    }
    /**
     * Retrieves the 2D array representing the card board.
     *
     * @return The 2D array representing the card board.
     */
    public Cell[][] getCardBoard() {
        return cardBoard;
    }
    /**
     * Retrieves the current score of the board.
     *
     * @return The current score of the board.
     */
    public int getScore() {
        return score;
    }
    /**
     * Retrieves the array containing counters for different angles.
     *
     * @return The array containing counters for different angles.
     */
    public int[] getAnglesCounter() {
        return anglesCounter;
    }
    /**
     * Sets the array containing counters for different angles.
     *
     * @param anglesCounter The array containing counters for different angles to set.
     */
    public void setAnglesCounter(int[] anglesCounter) {
        this.anglesCounter = anglesCounter;
    }
    /**
     * Sets the 2D array representing the card board.
     *
     * @param cardBoard The 2D array representing the card board to set.
     */
    public void setCardBoard(Cell[][] cardBoard) {
        this.cardBoard = cardBoard;
    }
    /**
     * Sets the list of cells on the board.
     *
     * @param cardList The list of cells on the board to set.
     */
    public void setCardList(ArrayList<Cell> cardList) {
        this.cardList = cardList;
    }
    /**
     * Sets the score of the board.
     *
     * @param score The score of the board to set.
     */
    public void setScore(int score) {
        this.score = score;
    }
}

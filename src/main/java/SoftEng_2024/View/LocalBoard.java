package SoftEng_2024.View;

import SoftEng_2024.Model.Player_and_Board.Cell;

import java.util.ArrayList;

public class LocalBoard {
    private Cell[][] cardBoard;
    private ArrayList<Cell> cardList;
    private int[] anglesCounter;
    private int score;
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

    public ArrayList<Cell> getCardList() {
        return cardList;
    }

    public Cell[][] getCardBoard() {
        return cardBoard;
    }

    public int getScore() {
        return score;
    }

    public int[] getAnglesCounter() {
        return anglesCounter;
    }

    public void setAnglesCounter(int[] anglesCounter) {
        this.anglesCounter = anglesCounter;
    }

    public void setCardBoard(Cell[][] cardBoard) {
        this.cardBoard = cardBoard;
    }

    public void setCardList(ArrayList<Cell> cardList) {
        this.cardList = cardList;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

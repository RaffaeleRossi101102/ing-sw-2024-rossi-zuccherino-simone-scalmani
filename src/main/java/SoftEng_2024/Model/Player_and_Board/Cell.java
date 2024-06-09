package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;

import java.io.Serializable;

public class Cell implements Serializable {
    private Card card;
    private boolean placeable;
    private int row;
    private int column;
    private boolean visited;
    //METHODS

    //CONSTRUCTOR
    public Cell(){
        this.placeable = false;
        this.visited=false;
    }
    //GETTERS
    public Card getCard() {
        return card;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean getPlaceable(){
        return placeable;
    }
    //SETTERS
    public void setCard(Card card) {
        this.card = card;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setPlaceable(boolean placeable) {
        this.placeable = placeable;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean getVisited(){
        return this.visited;
    }
    public void setVisited(boolean visited){
        this.visited = visited;
    }

}


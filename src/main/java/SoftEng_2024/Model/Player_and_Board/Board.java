package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.AngleIndexes;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.CellState;
import SoftEng_2024.Model.Observers.BoardObserver;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Represents the game board where cards can be placed and manipulated.
 */
public class Board {

    private final Cell[][] cardBoard;
    private final ArrayList<Cell> cardList;
    private final int[] anglesCounter;
    private int score;
    private BoardObserver boardObserver;

    //CONSTRUCTOR AND METHODS
    /**
     * Constructs a new game board with default dimensions and initializes its state.
     */
    public Board(){
        this.cardBoard= new Cell[85][85];
        for(int i=0; i< this.cardBoard.length; i++){
            for(int j=0; j< this.cardBoard[0].length; j++){
                this.cardBoard[i][j]=new Cell();
            }
        }
        this.cardList = new ArrayList<>();
        this.anglesCounter = new int[7];
        this.score = 0;
        this.cardBoard[42][42].setCellState(CellState.PLACEABLE);
    }

    //OBSERVER METHODS
    /**
     * Sets the observer for this board.
     *
     * @param o The observer instance to be set.
     */
    public void setObserver(BoardObserver o){
        boardObserver=o;
    }


    //metodo che setta le celle come available nel caso in cui la carta è giocata di front.
    //Fa dunque il controllo negli angoli: l'angolo della carta piazzata, nella direzione corrispondente
    //alla cella della matrice non deve essere invisibile
    /**
     * Checks and sets cells as placeable if the card is played front-facing.
     *
     * @param r          The row index of the cell.
     * @param c          The column index of the cell.
     * @param played     The card being played.
     * @param whichAngle The angle index indicating the direction.
     */
    private void checkIfPlaceable(int r,int c, Card played, AngleIndexes whichAngle){
        if(cardBoard[r][c].getCellState().equals(CellState.BANNED))
            return;
        if(played.getFlipped() | !played.getFront().getFrontAngles()[AngleIndexes.getIndex(whichAngle)].equals(Angles.INVISIBLE)) {
            cardBoard[r][c].setCellState(CellState.PLACEABLE);
        }
        else{
            cardBoard[r][c].setCellState(CellState.BANNED);
        }
    }

    /**
     * Sets cells around the played card as available for placement.
     *
     * @param r      The row index of the cell.
     * @param c      The column index of the cell.
     * @param played The card being played.
     */
    private void setAvailableCells(int r, int c,Card played) {

        if ((r - 1) >= 0 && (c - 1) >= 0) {
            checkIfPlaceable(r-1,c-1, played, AngleIndexes.UPLEFT);
        }

        if ((r + 1) < cardBoard.length && (c - 1) >= 0) {
            checkIfPlaceable(r+1,c-1, played, AngleIndexes.DOWNLEFT);
        }

        if ((r - 1) >= 0 && (c + 1) < cardBoard[0].length) {
            checkIfPlaceable(r - 1, c + 1, played, AngleIndexes.UPRIGHT);
        }

        if ((r + 1) < cardBoard.length && (c + 1) < cardBoard[0].length) {
            checkIfPlaceable(r+1,c+1, played, AngleIndexes.DOWNRIGHT);
        }

    }

    /**
     * Updates the counter for covered angles after placing a card.
     *
     * @param r          The row index of the cell.
     * @param c          The column index of the cell.
     * @param whichAngle The angle index indicating the direction.
     * @return The number of covered angles updated.
     */
    private int updateCounter(int r,int c, AngleIndexes whichAngle){

        Angles coveredAngle;
        //se non ho una carta nella direzione specificata ritorna
        if(cardBoard[r][c].getCard()==null)
            return 0;
        System.out.println("Sto settando il covered dell'angolo "+ whichAngle+ "e in posizione "+r+" "+c+" la carta è" + cardBoard[r][c].getCard());
        cardBoard[r][c].getCard().getFront().setCovered(AngleIndexes.getIndex(whichAngle), true);

        if(!cardBoard[r][c].getCard().getFlipped())
            coveredAngle=cardBoard[r][c].getCard().getFront().getFrontAngles()[AngleIndexes.getIndex(whichAngle)];
        else
            coveredAngle=cardBoard[r][c].getCard().getCardBackAnglesType()[AngleIndexes.getIndex(whichAngle)];
        if(!coveredAngle.equals(Angles.EMPTY))
            anglesCounter[Angles.getIndex(coveredAngle)]--;
        return 1;
    }


    //conta il numero di risorse coperte dopo il piazzamento
    /**
     * Counts the number of covered angles after placing a card.
     *
     * @param r          The row index of the cell.
     * @param c          The column index of the cell.
     * @param playedCard The card being played.
     * @return The total number of covered angles.
     */
    private int coveredAnglesCounter(int r, int c,Card playedCard) {
        System.out.println(boardObserver.getCallerNickname()+" "+ Arrays.toString(playedCard.getFront().getCovered()));
        //counter ha nelle prime 7 posizioni il numero di risorse/oggetti. All'ottava c'è il numero di
        //angoli coperti
        int counter = 0;
        int [] count=playedCard.getSumResources();
        //se in alto a sinistra c'è una cella
        if((r - 1)>=0 && (c - 1) >= 0) {
            counter+=updateCounter(r-1,c-1,AngleIndexes.DOWNRIGHT);
        }
        //se in basso a sinistra c'è una cella
        if((r+1)<cardBoard.length && c-1>=0) {
            counter+=updateCounter(r+1,c-1,AngleIndexes.UPRIGHT);
        }
        //se in alto a destra c'è una cella
        if((r-1)>=0 && c+1<cardBoard[0].length){
            counter+=updateCounter(r-1,c+1,AngleIndexes.DOWNLEFT);
        }
        //se in basso a destra c'è una cella
        if((r+1)<cardBoard.length && (c+1)<cardBoard[0].length){
            counter+=updateCounter(r+1,c+1,AngleIndexes.UPLEFT);
        }
        //sommo il numero di risorse/oggetti della carta al totale di risorse/oggetti nella board
        for(int i=0;i<7;i++){
            this.anglesCounter[i] += count[i];
        }
        System.out.println(Arrays.toString(playedCard.getFront().getCovered()));
        return counter;
    }

    /**
     * Exception indicating that the cell is not available for placing a card.
     */
    public static class notAvailableCellException extends Exception{}

    /**
     * Exception indicating that the necessary resources for placing a card are not available.
     */
    public static class necessaryResourcesNotAvailableException extends Exception{}


    //metodo che aggiorna la board con la carta piazzata
    /**
     * Updates the board by placing the played card at the specified cell.
     *
     * @param r      The row index of the cell.
     * @param c      The column index of the cell.
     * @param played The card being played.
     * @throws notAvailableCellException           If the cell is not available for placing the card.
     * @throws necessaryResourcesNotAvailableException If the necessary resources for placing the card are not available.
     */
    public void updateBoard(int r, int c, Card played) throws notAvailableCellException, necessaryResourcesNotAvailableException{
        int coveredAnglesCounter;

        //se posso piazzare la carta nella cella
        if(!cardBoard[r][c].getCellState().equals(CellState.PLACEABLE)){
            throw new notAvailableCellException();
        }
        if(!played.getFront().checkRequiredResources(anglesCounter, played)){
            throw new necessaryResourcesNotAvailableException();
        }
        //setto tutti i valori nella cella
        cardBoard[r][c].setCard(played);
        cardBoard[r][c].setRow(r);
        cardBoard[r][c].setColumn(c);
        cardBoard[r][c].setCellState(CellState.BANNED);
        coveredAnglesCounter = coveredAnglesCounter(r, c, played);
        //rendi disponibili, se possibile, al piazzamento le celle intorno
        setAvailableCells(r, c, played);
        //Se la carta l'ho giocata di front conta i punti
        if (!played.getFlipped()) {
            this.score += played.getFront().updateScoredPoints(anglesCounter, coveredAnglesCounter);
        }
        //aggiungi la carta piazzata alla lista
        this.cardList.add(cardBoard[r][c]);
        boardObserver.updatedBoard(this);
        //aggiungo alla lista la nuova cella con la nuova carta.

    }

    //Aggiorno il counter di risorse dopo il piazzamento sottraendo quelle coperte e aggiungendo quelle della carta
    //piazzata
    //GETTER
    /**
     * Retrieves the current count of covered angles.
     *
     * @return An array representing the count of covered angles.
     */
    public int[] getAnglesCounter() {
        return anglesCounter;
    }

    /**
     * Retrieves the current score of the board.
     *
     * @return The score of the board.
     */
    public int getScore(){
        return score;
    }

    /**
     * Retrieves the 2D array representing the card board.
     *
     * @return The card board.
     */
    public Cell[][] getCardBoard() {
        return cardBoard;
    }

    /**
     * Retrieves the list of cells containing cards on the board.
     *
     * @return The list of cells containing cards.
     */
    public ArrayList<Cell> getCardList() {
        return cardList;
    }

    /**
     * Sets the score of the board.
     *
     * @param score The score to set.
     */
    public void setScore(int score) {
        this.score = score;
    }
}
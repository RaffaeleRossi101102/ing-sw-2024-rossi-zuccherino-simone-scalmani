package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.AngleIndexes;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Observers.BoardObserver;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private Cell[][] cardBoard;
    private ArrayList<Cell> cardList;
    private int[] anglesCounter;
    private int score;
    private BoardObserver boardObserver;

    //CONSTRUCTOR AND METHODS
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
        this.cardBoard[42][42].setPlaceable(true);
    }
    //OBSERVER METHODS
    public void setObserver(BoardObserver o){
        boardObserver=o;
    }

    //Metodo che setta le celle nelle 4 direzioni come placeable, nel caso in cui la carta giocata
    //sia flipped (non controlla che l'angolo sia visible)
    private void setAvailableCellsFlipped(int r, int c){
        if((r-1)>=0 && (c-1)>=0) {
            if (cardBoard[r - 1][c - 1].getCard() == null) {
                cardBoard[r - 1][c - 1].setPlaceable(true);
                System.out.println("LA CELLA " + (r-1) + " " + (c-1) + " è piazzabile");
            }
        }
        if((r+1)<cardBoard.length && c-1>=0) {
            if (cardBoard[r + 1][c - 1].getCard() == null) {
                cardBoard[r + 1][c - 1].setPlaceable(true);
                System.out.println("LA CELLA " + (r+1) + " " + (c-1) + " è piazzabile");
            }
        }
        if((r-1)>=0 && c+1<cardBoard[0].length) {
            if (cardBoard[r - 1][c + 1].getCard() == null) {
                cardBoard[r - 1][c + 1].setPlaceable(true);
                System.out.println("LA CELLA " + (r-1) + " " + (c+1) + " è piazzabile");
            }
        }
        if((r+1)<cardBoard.length && (c+1)<cardBoard[0].length) {
            if (cardBoard[r + 1][c + 1].getCard() == null) {
                cardBoard[r + 1][c + 1].setPlaceable(true);
                System.out.println("LA CELLA " + (r+1) + " " + (c+1) + " è piazzabile");
            }
        }

    }
    //metodo che setta le celle come available nel caso in cui la carta è giocata di front.
    //Fa dunque il controllo negli angoli: l'angolo della carta piazzata, nella direzione corrispondente
    //alla cella della matrice non deve essere invisibile
    private void checkIfPlaceable(int r,int c, Card played, AngleIndexes whichAngle){
        if(!cardBoard[r][c].getCellState().equals(CellState.NOTPLACEABLE))
            return;
        if(played.getFlipped() | !played.getFront().getFrontAngles()[AngleIndexes.getIndex(whichAngle)].equals(Angles.INVISIBLE)) {
            cardBoard[r][c].setCellState(CellState.PLACEABLE);
        }
        else{
            cardBoard[r][c].setCellState(CellState.BANNED);
        }
    }
    private void setAvailableCells(int r, int c,Card played) {

        if ((r - 1) >= 0 && (c - 1) >= 0) {
            checkIfPlaceable(r-1,c-1, played, AngleIndexes.UPLEFT);
        }
        if((r+1)<cardBoard.length && c-1>=0) {
            if (cardBoard[r + 1][c - 1].getCard() == null &&
                    played.getFront().getFrontAngles()[AngleIndexes.getIndex(AngleIndexes.DOWNLEFT)] != Angles.INVISIBLE) {
                cardBoard[r + 1][c - 1].setPlaceable(true);
                System.out.println("LA CELLA " + (r+1) + " " + (c-1) + " è piazzabile");
            }
        }

        if ((r - 1) >= 0 && (c + 1) < cardBoard[0].length) {
            checkIfPlaceable(r - 1, c + 1, played, AngleIndexes.UPRIGHT);
        }

        if ((r + 1) < cardBoard.length && (c + 1) < cardBoard[0].length) {
            checkIfPlaceable(r+1,c+1, played, AngleIndexes.DOWNRIGHT);
        }

    }
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
    private int[] coveredAnglesCounter(int r, int c) {
        //counter ha nelle prime 7 posizioni il numero di risorse/oggetti. All'ottava c'è il numero di
        //angoli coperti
        int[] counter = new int[8];
        int index;
        //se in alto a sinistra c'è una cella
        if((r - 1)>=0 && (c - 1) >= 0) {
            counter+=updateCounter(r-1,c-1,AngleIndexes.DOWNRIGHT);
        }
        if((r+1)<cardBoard.length && c-1>=0) {
            counter+=updateCounter(r+1,c-1,AngleIndexes.UPRIGHT);
        }
        if((r-1)>=0 && c+1<cardBoard[0].length){
            if (cardBoard[r - 1][c + 1].getCard() != null) {
                counter[7]++;
                cardBoard[r - 1][c + 1].getCard().getFront().setCovered(AngleIndexes.getIndex(AngleIndexes.DOWNLEFT),true);
                if (!cardBoard[r - 1][c + 1].getCard().getFlipped()) {
                    index = Angles.getIndex(cardBoard[r - 1][c + 1].getCard().getFront().getFrontAngles()[AngleIndexes.getIndex(AngleIndexes.DOWNLEFT)]);
                    if (index < 7) {
                        counter[index] += 1;
                    }
                } else {
                    //index prende l'indice della risorsa in basso a destra
                    index = Angles.getIndex(cardBoard[r - 1][c + 1].getCard().getCardBackAnglesType()[AngleIndexes.getIndex(AngleIndexes.DOWNLEFT)]);
                    if (index < 7) {
                        counter[index] += 1;
                    }
                }

            }
        }
        if((r+1)<cardBoard.length && (c+1)<cardBoard[0].length){
            if(cardBoard[r+1][c+1].getCard() != null) {
                counter[7]++;
                cardBoard[r + 1][c + 1].getCard().getFront().setCovered(AngleIndexes.getIndex(AngleIndexes.UPLEFT),true);
                if (!cardBoard[r + 1][c + 1].getCard().getFlipped()) {
                    index = Angles.getIndex(cardBoard[r + 1][c + 1].getCard().getFront().getFrontAngles()[AngleIndexes.getIndex(AngleIndexes.UPLEFT)]);
                    if (index < 7) {
                        counter[index] += 1;
                    }
                } else {
                    //index prende l'indice della risorsa in basso a destra
                    index = Angles.getIndex(cardBoard[r + 1][c + 1].getCard().getCardBackAnglesType()[AngleIndexes.getIndex(AngleIndexes.UPLEFT)]);
                    if (index < 7) {
                        counter[index] += 1;
                    }
                }

            }
        }
        //sommo il numero di risorse/oggetti della carta al totale di risorse/oggetti nella board
        for(int i=0;i<7;i++){
            this.anglesCounter[i] += count[i];
        }
        System.out.println(Arrays.toString(playedCard.getFront().getCovered()));
        return counter;
    }


    public static class notAvailableCellException extends Exception{}

    public static class necessaryResourcesNotAvailableException extends Exception{}

    //metodo che aggiorna la board con la carta piazzata
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
//    public void updateCountersz(int[] counter, Card played){
//        int[] count;//= new int[7];
//        // Angles[] angles;
//        // toglie da anglescounter le risorse/oggetti coperte dalla nuova carta
//        //System.out.println("Dentro updateCounters il counter di risorse nella board è : " + counter.length);
//        for(int i=0; i<7; i++){
//            this.anglesCounter[i] -= counter[i];
//
//        }
//        //salvo in count il numero di risorse nella carta
//        count=played.getSumResources();
//        //System.out.println("la lunghezza di count è: " + count.length);
//
//        //sommo il numero di risorse/oggetti della carta al totale di risorse/oggetti nella board
//        System.out.print("Le risorse sono: [");
//        for(int i=0;i<7;i++){
//            this.anglesCounter[i] += count[i];
//            if (i<6) System.out.print(this.anglesCounter[i]+" - "); //reso carino
//            else System.out.print(this.anglesCounter[i]);
//        }
//        System.out.println("]");
//    }
    //public void addObservers(ModelObserver ob){
//        boardObservers.add()
//    }
    //GETTER
    public int[] getAnglesCounter() {
        return anglesCounter;
    }
    public int getScore(){
        return score;
    }

    public Cell[][] getCardBoard() {
        return cardBoard;
    }

    public ArrayList<Cell> getCardList() {
        return cardList;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
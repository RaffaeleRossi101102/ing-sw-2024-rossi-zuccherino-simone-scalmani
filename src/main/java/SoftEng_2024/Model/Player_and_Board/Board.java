package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.AngleIndexes;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Observers.BoardObserver;

import java.util.ArrayList;

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
    private void setAvailableCells(int r, int c,Card played){
        if((r-1)>=0 && (c-1)>=0) {
            if (cardBoard[r - 1][c - 1].getCard() ==null  &&
                    played.getFront().getFrontAngles()[AngleIndexes.getIndex(AngleIndexes.UPLEFT)] != Angles.INVISIBLE) {
                System.out.println("LA CELLA " + (r-1) + " " + (c-1) + " è piazzabile");
                cardBoard[r - 1][c - 1].setPlaceable(true);
            }
        }
        if((r+1)<cardBoard.length && c-1>=0) {
            if (cardBoard[r + 1][c - 1].getCard() == null &&
                    played.getFront().getFrontAngles()[AngleIndexes.getIndex(AngleIndexes.DOWNLEFT)] != Angles.INVISIBLE) {
                cardBoard[r + 1][c - 1].setPlaceable(true);
                System.out.println("LA CELLA " + (r+1) + " " + (c-1) + " è piazzabile");
            }
        }
        if((r-1)>=0 && c+1<cardBoard[0].length) {
            if (cardBoard[r - 1][c + 1].getCard() == null &&
                    played.getFront().getFrontAngles()[AngleIndexes.getIndex(AngleIndexes.UPRIGHT)] != Angles.INVISIBLE) {
                cardBoard[r - 1][c + 1].setPlaceable(true);
                System.out.println("LA CELLA " + (r-1) + " " + (c+1) + " è piazzabile");
            }
        }
        if((r+1)<cardBoard.length && (c+1)<cardBoard[0].length) {
            if (cardBoard[r + 1][c + 1].getCard() == null &&
                    played.getFront().getFrontAngles()[AngleIndexes.getIndex(AngleIndexes.DOWNRIGHT)] != Angles.INVISIBLE) {
                cardBoard[r + 1][c + 1].setPlaceable(true);
                System.out.println("LA CELLA " + (r+1) + " " + (c+1) + " è piazzabile");
            }
        }

    }

    //conta il numero di risorse coperte dopo il piazzamento
    private int[] coveredAnglesCounter(int r, int c) {
        //counter ha nelle prime 7 posizioni il numero di risorse/oggetti. All'ottava c'è il numero di
        //angoli coperti
        int[] counter = new int[8];
        int index;
        //se in alto a sinistra c'è una cella
        if((r - 1)>=0 && (c - 1) >= 0) {
            //se ho piazzato la carta sopra un'altra
            if (cardBoard[r - 1][c - 1].getCard() != null) {
                counter[7]++;
                cardBoard[r - 1][c - 1].getCard().getFront().setCovered(AngleIndexes.getIndex(AngleIndexes.DOWNRIGHT),true);
                //se la carta su cui ho piazzato è flippata
                if (!cardBoard[r - 1][c - 1].getCard().getFlipped()) {
                    //prendi l'angolo che ho coperto
                    index = Angles.getIndex(cardBoard[r - 1][c - 1].getCard().getFront().getFrontAngles()[AngleIndexes.getIndex(AngleIndexes.DOWNRIGHT)]);
                    //Se l'angolo non è vuoto, aumenta il counter
                    if (index < 7) {
                        counter[index] += 1;
                    }
                    //se la carta non è flippata
                } else {
                    //index prende l'indice della risorsa in basso a destra
                    index = Angles.getIndex(cardBoard[r - 1][c - 1].getCard().getCardBackAnglesType()[AngleIndexes.getIndex(AngleIndexes.DOWNRIGHT)]);
                    if (index < 7) {
                        counter[index] += 1;
                    }
                }

            }
        }
        if((r+1)<cardBoard.length && c-1>=0) {
            //Ripete per tutte le direzioni
            if (cardBoard[r + 1][c - 1].getCard() != null) {
                counter[7]++;
                cardBoard[r + 1][c - 1].getCard().getFront().setCovered(AngleIndexes.getIndex(AngleIndexes.UPRIGHT),true);
                if (!cardBoard[r + 1][c - 1].getCard().getFlipped()) {
                    index = Angles.getIndex(cardBoard[r + 1][c - 1].getCard().getFront().getFrontAngles()[AngleIndexes.getIndex(AngleIndexes.UPRIGHT)]);
                    if (index < 7) {
                        counter[index] += 1;
                    }
                } else {
                    //index prende l'indice della risorsa in basso a destra
                    index = Angles.getIndex(cardBoard[r + 1][c - 1].getCard().getCardBackAnglesType()[AngleIndexes.getIndex(AngleIndexes.UPRIGHT)]);
                    if (index < 7) {
                        counter[index] += 1;
                    }
                }

            }
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
        return counter;
    }


    public static class notAvailableCellException extends Exception{}

    public static class necessaryResourcesNotAvailableException extends Exception{}

    //metodo che aggiorna la board con la carta piazzata
    public boolean updateBoard(int r, int c, Card played) throws notAvailableCellException, necessaryResourcesNotAvailableException{
        int[] coveredAnglesCounter;// = new int[7];
        boolean placed=false;
        //se posso piazzare la carta nella cella
        if (cardBoard[r][c].getPlaceable()) {
            //e se nella catena ho le risorse necessarie per piazzarla
            if(played.getFront().checkRequiredResources(anglesCounter, played)){
                //setto tutti i valori nella cella
                cardBoard[r][c].setCard(played);
                cardBoard[r][c].setRow(r);
                cardBoard[r][c].setColumn(c);
                cardBoard[r][c].setPlaceable(false);
                //System.out.println("HO INSERITO LA CARTA DENTRO LA MATRICE");
                placed = true;
                //Se la carta l'ho giocata di back
                if (played.getFlipped()) {
                    //Rendi disponibili per il piazzamento le altre celle
                    setAvailableCellsFlipped(r, c);
                    //conta le risorse coperte
                    coveredAnglesCounter = coveredAnglesCounter(r, c);
                    //aggiorna il counter delle risorse totali
                    updateCounters(coveredAnglesCounter, played);
                    //System.out.println("punteggio=" + this.score); //da eliminare la rica in questione
                } else {
                    //stessa cosa ma per carta giocata di front
                    setAvailableCells(r, c, played);
                    coveredAnglesCounter = coveredAnglesCounter(r, c);
                    updateCounters(coveredAnglesCounter, played);
                    //controlla l'eventuale punteggio ottenuto al piazzamento e aggiungilo
                    this.score += played.getFront().updateScoredPoints(anglesCounter, coveredAnglesCounter[7]);
                    System.out.println("punteggio=" + this.score); //da eliminare la rica in questione
                }
                //aggiungi la carta piazzata alla lista
                this.cardList.add(cardBoard[r][c]);
                boardObserver.updatedBoard(this);
                //aggiungo alla lista la nuova cella con la nuova carta.
            } else  throw new necessaryResourcesNotAvailableException();

        } else throw new notAvailableCellException();
        return placed;
    }
    //Aggiorno il counter di risorse dopo il piazzamento sottraendo quelle coperte e aggiungendo quelle della carta
    //piazzata
    public void updateCounters(int[] counter, Card played){
        int[] count;//= new int[7];
        // Angles[] angles;
        // toglie da anglescounter le risorse/oggetti coperte dalla nuova carta
        //System.out.println("Dentro updateCounters il counter di risorse nella board è : " + counter.length);
        for(int i=0; i<7; i++){
            this.anglesCounter[i] -= counter[i];

        }
        //salvo in count il numero di risorse nella carta
        count=played.getSumResources();
        //System.out.println("la lunghezza di count è: " + count.length);

        //sommo il numero di risorse/oggetti della carta al totale di risorse/oggetti nella board
        System.out.print("Le risorse sono: [");
        for(int i=0;i<7;i++){
            this.anglesCounter[i] += count[i];
            if (i<6) System.out.print(this.anglesCounter[i]+" - "); //reso carino
            else System.out.print(this.anglesCounter[i]);
        }
        System.out.println("]");
    }
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
package SoftEng_2024.Model.GoalCard;


import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Player_and_Board.Cell;
import SoftEng_2024.Model.Enums.Angles;

import java.util.List;


public class DiagonalGoalCard extends GoalCard {

    private final Angles resource;
    private boolean tiltedForward;

    public DiagonalGoalCard(Angles resource, int points, boolean tiltedForward, String goaltype, int cardID) {
        super(points, goaltype, cardID);
        this.resource=resource;
        this.tiltedForward = tiltedForward;
    }

    @Override
    public int calcScore(Board playerBoard) {

        int res = 0;
        Cell[][] localBoard = playerBoard.getCardBoard();
        Cell starterCell;
        //copio la lista di celle della board dentro cellArrayList
        List<Cell> cellArrayList = playerBoard.getCardList();
        //se la diagonale è discendente
        if (!tiltedForward) {
            //per ogni cella dentro la lista
            for (Cell cell : cellArrayList) {
                if (!cell.getVisited() && cell.getCard().getResources()[4].equals(resource)){
                    int count=1;
                    starterCell = cell;
                    starterCell.setVisited(true);
                    //dentro il while risalgo verso l'alto/sinistra finché non trovo la carta da cui inizia la diagonale
                    while (localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getCard()!= null &&
                            (localBoard[starterCell.getRow()-1][starterCell.getColumn()-1].getCard().getResources()[4].equals(resource) && starterCell.getRow() >= 1 &&
                                    starterCell.getColumn() >= 1)) {
                        //sposto la cella di ricerca, la setto visitata e la aggiungo al counter di carte
                        starterCell = localBoard[starterCell.getRow()-1][starterCell.getColumn()-1];
                        starterCell.setVisited(true);
                        count ++;
                    }

                    starterCell = cell;
                    //scendo in basso a destra e conto le carte che fanno parte della diagonale
                    while (localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getCard()!= null &&
                            (localBoard[starterCell.getRow()+1][starterCell.getColumn()+1].getCard().getResources()[4].equals(resource) && starterCell.getRow() <= localBoard.length-2 &&
                                    starterCell.getColumn() <= localBoard[0].length-2)) {

                        starterCell = localBoard[starterCell.getRow()+1][starterCell.getColumn()+1];
                        starterCell.setVisited(true);
                        count ++;

                    }

                    res += (int)(count/3);

                }
            }
        //se la diagonale é ascendente
        } else {
            for (Cell cell : cellArrayList) {
                if (!cell.getVisited() && cell.getCard().getResources()[4].equals(resource)){
                    int count=1;
                    starterCell = cell;
                    starterCell.setVisited(true);
                    //dentro il while risalgo verso l'alto finché non trovo la carta da cui inizia la diagonale
                    while (localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getCard()!= null &&
                            (localBoard[starterCell.getRow()-1][starterCell.getColumn()+1].getCard().getResources()[4].equals(resource) && starterCell.getRow() >= 1 &&
                                    starterCell.getColumn() <= localBoard[0].length-2)) {

                        starterCell = localBoard[starterCell.getRow()-1][starterCell.getColumn()+1];
                        starterCell.setVisited(true);
                        count ++;
                    }
                    starterCell = cell;
                    while (localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getCard()!= null &&
                            (localBoard[starterCell.getRow()+1][starterCell.getColumn()-1].getCard().getResources()[4].equals(resource) && starterCell.getRow() <= localBoard.length-2 &&
                                    starterCell.getColumn() >= 1)) {

                        starterCell = localBoard[starterCell.getRow()+1][starterCell.getColumn()-1];
                        starterCell.setVisited(true);
                        count ++;

                    }

                    res += (int) (count/3);
                }
            }
        }
        for(Cell cella : cellArrayList) cella.setVisited(false);
        return res * this.getPoints();
    }
}

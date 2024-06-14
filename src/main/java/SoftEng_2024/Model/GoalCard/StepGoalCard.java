package SoftEng_2024.Model.GoalCard;

import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Player_and_Board.Cell;
import SoftEng_2024.Model.Enums.Angles;

import java.util.List;

public class StepGoalCard extends GoalCard {
    private final Angles baseResource;
    private final Angles sideResource;
    private final boolean baseTop;
    private final boolean sideLeft;

    public StepGoalCard(Angles baseResource, Angles sideResource, boolean baseTop, boolean sideLeft, int points, String goalType, int cardID) {
        super(points, goalType, cardID);
        this.baseResource = baseResource;
        this.sideResource = sideResource;
        this.baseTop = baseTop;
        this.sideLeft = sideLeft;
    }

    @Override
    public int calcScore(Board playerBoard) {
        int res = 0;
        Cell[][] localBoard = playerBoard.getCardBoard();
        List<Cell> cellArrayList = playerBoard.getCardList();

        for (Cell cell : cellArrayList) {
            if (!cell.getVisited() && cell.getCard().getResources()[4].equals(baseResource)) {
                int counterSide = 0;
                int counterPattern = 0;

                if (isValidStartingCell(cell, localBoard)) {
                    cell.setVisited(true);

                    counterSide += countSideCards(localBoard, cell, true);
                    counterPattern += countPatterns(localBoard, cell, true);

                    counterSide += countSideCards(localBoard, cell, false);
                    counterPattern += countPatterns(localBoard, cell, false);

                    res += (counterSide / 2) + counterPattern;
                }
            }
        }

        resetVisitedCells(cellArrayList);
        return res * this.getPoints();
    }

    private boolean isValidStartingCell(Cell cell, Cell[][] localBoard) {
        if (!baseTop) {
            return (sideLeft && cell.getRow() > 2 && cell.getColumn() > 0)
                    || (!sideLeft && cell.getRow() > 2 && cell.getColumn() < localBoard[0].length);
        } else {
            return (sideLeft && cell.getRow() < localBoard.length - 3 && cell.getColumn() > 0)
                    || (!sideLeft && cell.getRow() < localBoard.length - 3 && cell.getColumn() < localBoard[0].length);
        }
    }

    private int countSideCards(Cell[][] localBoard, Cell starterCell, boolean down) {
        int counterSide = 0;
        int rowIncrement = down ? 2 : -2;
        int sideRowIncrement = down ? 1 : -1;
        int sideColIncrement = sideLeft ? -1 : 1;

        while (isValidStep(localBoard, starterCell, rowIncrement, sideRowIncrement, sideColIncrement)) {
            localBoard[starterCell.getRow() + sideRowIncrement][starterCell.getColumn() + sideColIncrement].setVisited(true);
            counterSide++;
            starterCell = localBoard[starterCell.getRow() + rowIncrement][starterCell.getColumn()];
            starterCell.setVisited(true);
        }

        return counterSide;
    }

    private boolean isValidStep(Cell[][] localBoard, Cell starterCell, int rowIncrement, int sideRowIncrement, int sideColIncrement) {
        int newRow = starterCell.getRow() + rowIncrement;
        int newSideRow = starterCell.getRow() + sideRowIncrement;
        int newSideCol = starterCell.getColumn() + sideColIncrement;

        return newRow >= 0 && newRow < localBoard.length
                && newSideRow >= 0 && newSideRow < localBoard.length
                && newSideCol >= 0 && newSideCol < localBoard[0].length
                && localBoard[newRow][starterCell.getColumn()].getCard() != null
                && localBoard[newRow][starterCell.getColumn()].getCard().getResources()[4].equals(baseResource)
                && !localBoard[newRow][starterCell.getColumn()].getVisited()
                && localBoard[newSideRow][newSideCol].getCard() != null
                && localBoard[newSideRow][newSideCol].getCard().getResources()[4].equals(sideResource)
                && !localBoard[newSideRow][newSideCol].getVisited();
    }

    private int countPatterns(Cell[][] localBoard, Cell starterCell, boolean down) {
        int sideRowIncrement = down ? 1 : -1;
        int sideColIncrement = sideLeft ? -1 : 1;

        Cell sideCell = localBoard[starterCell.getRow() + sideRowIncrement][starterCell.getColumn() + sideColIncrement];
        if (sideCell.getCard() != null && sideCell.getCard().getResources()[4].equals(sideResource) && !sideCell.getVisited()) {
            if (down) {
                return isValidPattern(localBoard, sideCell, 2) ? 1 : 0;
            } else {
                return isValidPattern(localBoard, sideCell, -2) ? 1 : 0;
            }
        }
        return 0;
    }

    private boolean isValidPattern(Cell[][] localBoard, Cell starterCell, int rowIncrement) {
        int newRow = starterCell.getRow() + rowIncrement;

        return newRow >= 0 && newRow < localBoard.length
                && localBoard[newRow][starterCell.getColumn()].getCard() != null
                && localBoard[newRow][starterCell.getColumn()].getCard().getResources()[4].equals(sideResource)
                && !localBoard[newRow][starterCell.getColumn()].getVisited();
    }

    private void resetVisitedCells(List<Cell> cellArrayList) {
        for (Cell cell : cellArrayList) {
            cell.setVisited(false);
        }
    }
}

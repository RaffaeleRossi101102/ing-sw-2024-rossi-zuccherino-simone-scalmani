package SoftEng_2024.Model.GoalCard;

import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Player_and_Board.Cell;
import SoftEng_2024.Model.Enums.Angles;

import java.util.List;

/**
 * Represents a step goal card in the game, which defines a specific pattern
 * of resources on the board to achieve and score points.
 */
public class StepGoalCard extends GoalCard {
    private final Angles baseResource;
    private final Angles sideResource;
    private final boolean baseTop;
    private final boolean sideLeft;

    /**
     * Constructs a StepGoalCard with specified base and side resources, orientation flags,
     * points, goal type, and card ID.
     *
     * @param baseResource The resource type required on the base of the pattern.
     * @param sideResource The resource type required on the sides of the pattern.
     * @param baseTop Indicates whether the base of the pattern is at the top (true) or bottom (false).
     * @param sideLeft Indicates whether the side of the pattern is on the left (true) or right (false).
     * @param points The points awarded for completing this goal.
     * @param goalType The type of the goal card.
     * @param cardID The unique identifier of the goal card.
     */
    public StepGoalCard(Angles baseResource, Angles sideResource, boolean baseTop, boolean sideLeft, int points, String goalType, int cardID) {
        super(points, goalType, cardID);
        this.baseResource = baseResource;
        this.sideResource = sideResource;
        this.baseTop = baseTop;
        this.sideLeft = sideLeft;
    }

    /**
     * Calculates the score for this goal card based on the player's board.
     * Scores are awarded for valid patterns formed using the specified resources.
     *
     * @param playerBoard The board of the player on which to calculate the score.
     * @return The calculated score for this goal card.
     */
    @Override
    public int calcScore(Board playerBoard) {
        int res = 0;
        Cell[][] localBoard = playerBoard.getCardBoard();
        List<Cell> cellArrayList = playerBoard.getCardList();

        for (Cell cell : cellArrayList) {
            if (!cell.getVisited() && cell.getCard().getCardBackAnglesType()[4].equals(baseResource)) {
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

    /**
     * Checks if the given cell is a valid starting point for the step pattern.
     *
     * @param cell The cell to check.
     * @param localBoard The local representation of the player's board.
     * @return True if the cell is a valid starting point, false otherwise.
     */
    private boolean isValidStartingCell(Cell cell, Cell[][] localBoard) {
        if (!baseTop) {
            return (sideLeft && cell.getRow() > 2 && cell.getColumn() > 0)
                    || (!sideLeft && cell.getRow() > 2 && cell.getColumn() < localBoard[0].length);
        } else {
            return (sideLeft && cell.getRow() < localBoard.length - 3 && cell.getColumn() > 0)
                    || (!sideLeft && cell.getRow() < localBoard.length - 3 && cell.getColumn() < localBoard[0].length);
        }
    }

    /**
     * Counts the number of valid side cards in the specified direction from the starter cell.
     *
     * @param localBoard The local representation of the player's board.
     * @param starterCell The cell from which to start counting.
     * @param down True to count downwards, false to count upwards.
     * @return The count of valid side cards.
     */
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

    /**
     * Checks if the step from the starter cell is valid in terms of the pattern rules.
     *
     * @param localBoard The local representation of the player's board.
     * @param starterCell The cell from which to start the step.
     * @param rowIncrement The row increment for the step.
     * @param sideRowIncrement The row increment for the side cell.
     * @param sideColIncrement The column increment for the side cell.
     * @return True if the step is valid, false otherwise.
     */
    private boolean isValidStep(Cell[][] localBoard, Cell starterCell, int rowIncrement, int sideRowIncrement, int sideColIncrement) {
        int newRow = starterCell.getRow() + rowIncrement;
        int newSideRow = starterCell.getRow() + sideRowIncrement;
        int newSideCol = starterCell.getColumn() + sideColIncrement;

        return newRow >= 0 && newRow < localBoard.length
                && newSideRow >= 0 && newSideRow < localBoard.length
                && newSideCol >= 0 && newSideCol < localBoard[0].length
                && localBoard[newRow][starterCell.getColumn()].getCard() != null
                && localBoard[newRow][starterCell.getColumn()].getCard().getCardBackAnglesType()[4].equals(baseResource)
                && !localBoard[newRow][starterCell.getColumn()].getVisited()
                && localBoard[newSideRow][newSideCol].getCard() != null
                && localBoard[newSideRow][newSideCol].getCard().getCardBackAnglesType()[4].equals(sideResource)
                && !localBoard[newSideRow][newSideCol].getVisited();
    }

    /**
     * Counts the valid patterns from the starter cell in the specified direction.
     *
     * @param localBoard The local representation of the player's board.
     * @param starterCell The cell from which to start counting.
     * @param down True to count downwards, false to count upwards.
     * @return The count of valid patterns.
     */
    private int countPatterns(Cell[][] localBoard, Cell starterCell, boolean down) {
        int sideRowIncrement = down ? 1 : -1;
        int sideColIncrement = sideLeft ? -1 : 1;

        Cell sideCell = localBoard[starterCell.getRow() + sideRowIncrement][starterCell.getColumn() + sideColIncrement];
        if (sideCell.getCard() != null && sideCell.getCard().getCardBackAnglesType()[4].equals(sideResource) && !sideCell.getVisited()) {
            if (down) {
                return isValidPattern(localBoard, sideCell, 2) ? 1 : 0;
            } else {
                return isValidPattern(localBoard, sideCell, -2) ? 1 : 0;
            }
        }
        return 0;
    }

    /**
     * Checks if the specified pattern from the starter cell is valid.
     *
     * @param localBoard The local representation of the player's board.
     * @param starterCell The cell from which to start checking the pattern.
     * @param rowIncrement The row increment for the pattern.
     * @return True if the pattern is valid, false otherwise.
     */
    private boolean isValidPattern(Cell[][] localBoard, Cell starterCell, int rowIncrement) {
        int newRow = starterCell.getRow() + rowIncrement;

        return newRow >= 0 && newRow < localBoard.length
                && localBoard[newRow][starterCell.getColumn()].getCard() != null
                && localBoard[newRow][starterCell.getColumn()].getCard().getCardBackAnglesType()[4].equals(sideResource)
                && !localBoard[newRow][starterCell.getColumn()].getVisited();
    }

    /**
     * Resets the visited status of all cells in the provided list.
     *
     * @param cellArrayList The list of cells to reset.
     */
    private void resetVisitedCells(List<Cell> cellArrayList) {
        for (Cell cell : cellArrayList) {
            cell.setVisited(false);
        }
    }
}

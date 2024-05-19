package SoftEng_2024;

import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.Model.Player_and_Board.Cell;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.Front;
import SoftEng_2024.Model.Fronts.ResourceFront;

public class DiagonalGoalCardNotTilted {
    public static void main(String[] args) throws Board.notAvailableCellException, Board.necessaryResourcesNotAvailableException {
        //FRONT OF CARDS
        Angles[] frontAngles1 = {Angles.FEATHER, Angles.FEATHER, Angles.INSECTS, Angles.INSECTS};
        Angles[] frontAngles2 = {Angles.INSECTS, Angles.INSECTS, Angles.EMPTY, Angles.SCROLL};
        Angles[] frontAngles3 = {Angles.FUNGI, Angles.FEATHER, Angles.FUNGI, Angles.INSECTS};
        Angles[] frontAngles4 = {Angles.INK, Angles.EMPTY, Angles.PLANTS, Angles.INSECTS};
        Angles[] frontAngles5 = {Angles.ANIMALS, Angles.ANIMALS, Angles.SCROLL, Angles.EMPTY};
        Angles[] frontAngles6 = {Angles.SCROLL, Angles.EMPTY, Angles.ANIMALS, Angles.ANIMALS};
        Angles[] frontAngles7 = {Angles.ANIMALS, Angles.INSECTS, Angles.ANIMALS, Angles.INK};
        Angles[] frontAngles8 = {Angles.FEATHER, Angles.ANIMALS, Angles.SCROLL, Angles.ANIMALS};
        Angles[] frontAngles9 = {Angles.EMPTY, Angles.INSECTS, Angles.INK, Angles.ANIMALS};
        // BACK OF CARDS
        Angles[] resources = {Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.INSECTS};
        Angles[] resources2 = {Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.INSECTS};
        Angles[] resources3 = {Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.INSECTS};
        Angles[] resources4 = {Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.INSECTS};
        Angles[] resources5 = {Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.INSECTS};
        Angles[] resources6 = {Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.INSECTS};
        Angles[] resources7 = {Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.INSECTS};
        Angles[] resources8 = {Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.INSECTS};
        Angles[] resources9 = {Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.ANIMALS};
        Angles[] resourcesStarter = {Angles.PLANTS, Angles.ANIMALS, Angles.FUNGI, Angles.INSECTS, Angles.EMPTY};
        //DECLARATION COVERED ANGLES
        boolean[] covered = {false, false, false, false};
        //FRONT DECLARATION
        Front resourceFront1 = (ResourceFront) new ResourceFront(frontAngles1, 1, covered);
        Front resourceFront2 = (ResourceFront) new ResourceFront(frontAngles2, 0, covered);
        Front resourceFront3 = (ResourceFront) new ResourceFront(frontAngles3, 1, covered);
        Front resourceFront4 = (ResourceFront) new ResourceFront(frontAngles4, 1, covered);
        Front resourceFront5 = (ResourceFront) new ResourceFront(frontAngles5, 0, covered);
        Front resourceFront6 = (ResourceFront) new ResourceFront(frontAngles6, 1, covered);
        Front resourceFront7 = (ResourceFront) new ResourceFront(frontAngles7, 0, covered);
        Front resourceFront8 = (ResourceFront) new ResourceFront(frontAngles8, 0, covered);
        Front resourceFront9 = (ResourceFront) new ResourceFront(frontAngles9, 1, covered);

        //CARD DECLARATION
        Card resourceCard1 = (ResourceCard) new ResourceCard(resourceFront1, false, resources);
        Card resourceCard2 = (ResourceCard) new ResourceCard(resourceFront2, false, resources2);
        Card resourceCard3 = (ResourceCard) new ResourceCard(resourceFront3, false, resources3);
        Card resourceCard4 = (ResourceCard) new ResourceCard(resourceFront4, true, resources4);
        Card resourceCard5 = (ResourceCard) new ResourceCard(resourceFront5, false, resources5);
        Card resourceCard6 = (ResourceCard) new ResourceCard(resourceFront6, true, resources6);
        Card resourceCard7 = (ResourceCard) new ResourceCard(resourceFront7, false, resources7);
        Card resourceCard8 = (ResourceCard) new ResourceCard(resourceFront8, false, resources8);
        Card resourceCard9 = (ResourceCard) new ResourceCard(resourceFront9, false, resources9);

        //STARTER CARD
        Angles[] starterAngles = {Angles.ANIMALS, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI, Angles.FUNGI, Angles.EMPTY, Angles.EMPTY};
        Angles[] starterResources = {Angles.PLANTS, Angles.ANIMALS, Angles.FUNGI, Angles.INSECTS, Angles.EMPTY};
        Front starterFront = (ResourceFront) new ResourceFront(starterAngles, 0, covered);
        Card starterCard = (StarterCard) new StarterCard(starterFront, true, starterResources);

        //GOAL CARD
        //GoalCard tiltedCard = (DiagonalGoalCard) new DiagonalGoalCard(Angles.INSECTS, 1, true);

        Cell[][] matrice = new Cell[85][85];
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[0].length; j++) {
                matrice[i][j] = new Cell();
            }
        }
        Board board = new Board();


        //PLACING THE STARTER CARD
        matrice[42][42].setPlaceable(true);
        board.updateBoard(42, 42, starterCard);

        //DIAGONAL WITH STARTER IN THE MIDDLE EXPECTED SCORE: 2
        board.updateBoard(41, 43, resourceCard1);
        board.updateBoard(40, 44, resourceCard2);
        board.updateBoard(39, 45, resourceCard3);
        board.updateBoard(38, 46, resourceCard4);
        board.updateBoard(43, 41, resourceCard5);
        board.updateBoard(44, 40, resourceCard6);
        board.updateBoard(45, 39, resourceCard7);
        //System.out.println(tiltedCard.calcScore(board));

        //DIAGONAL WITHOUT ANY DIFFERENT CARD IN THE MIDDLE EXPECTED SCORE: 2 (UPRIGHT)
        /*
        board.updateBoard(41,43,resourceCard1);
        board.updateBoard(40,44,resourceCard2);
        board.updateBoard(39,45,resourceCard3);
        board.updateBoard(38,46,resourceCard4);
        board.updateBoard(37,47,resourceCard5);
        board.updateBoard(36,48,resourceCard6);
        board.updateBoard(35,49,resourceCard7);
        System.out.println(tiltedCard.calcScore(board));
         */

        //DIAGONAL WITHOUT ANY DIFFERENT CARD IN THE MIDDLE EXPECTED SCORE: 2 (DOWNLEFT)
        /*
        board.updateBoard(43,41,resourceCard1);
        board.updateBoard(44,40,resourceCard2);
        board.updateBoard(45,39,resourceCard3);
        board.updateBoard(46,38,resourceCard4);
        board.updateBoard(47,37,resourceCard5);
        board.updateBoard(48,36,resourceCard6);
        board.updateBoard(49,35,resourceCard7);
        System.out.println(tiltedCard.calcScore(board));
        */

        //SINGLE TILTED EXPECTED SCORE: 1 (UPRIGHT)
        /*
        board.updateBoard(41,43,resourceCard1);
        board.updateBoard(40,44,resourceCard2);
        board.updateBoard(39,45,resourceCard3);
        System.out.println(tiltedCard.calcScore(board));
        */

        //SINGLE TILTED EXPECTED SCORE: 1 (DOWNLEFT)
        /*
        board.updateBoard(43,41,resourceCard1);
        board.updateBoard(44,40,resourceCard2);
        board.updateBoard(45,39,resourceCard3);
        System.out.println(tiltedCard.calcScore(board));
        */
    }
}
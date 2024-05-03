package SoftEng_2024.Model;

import SoftEng_2024.Controller.GameInit;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.ResourceFront;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoalCardTest {

    @Test
    void ResourceCalcScore() {

        // TEST USING PLANTS AS RESOURCE FOR THE CALCULATION

        ResourceGoalCard testGoal = new ResourceGoalCard(Angles.PLANTS, 2, String.format("Get %s points for each triplet of mushrooms on the board", 2));

        GameInit testGameInit = new GameInit();
        testGameInit.gameInit();
        Board testBoard = new Board();

        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard, "paolo"));

        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getGoldDeck().poll());

        try {
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(42, 42, testGameInit.getGame().getPlayers().get(0).getHand().get(0));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(43, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(1));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(41, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(2));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(44, 44, testGameInit.getGame().getPlayers().get(0).getHand().get(3));
        } catch (Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException ignored) {
        }

        int[] anglesCounter = testBoard.getAnglesCounter();

        int score = testGoal.calcScore(testBoard);
        int expectedScore = (anglesCounter[Angles.PLANTS.ordinal()] / 3) * testGoal.getPoints();
        assertEquals(expectedScore, score);
    }

    @Test
    void DoubleObjectsCalcScore() {

        // TEST USING INK AS RESOURCE FOR THE CALCULATION

        ObjectsGoalCard testGoal = new ObjectsGoalCard(new Angles[]{Angles.INK, Angles.INK}, 2, String.format("Get %s points for each pair of feathers on the board", 2));

        GameInit testGameInit = new GameInit();
        testGameInit.gameInit();
        Board testBoard = new Board();

        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard, "paolo"));

        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getGoldDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getGoldDeck().poll());

        try {
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(42, 42, testGameInit.getGame().getPlayers().get(0).getHand().get(0));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(43, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(1));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(41, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(2));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(44, 44, testGameInit.getGame().getPlayers().get(0).getHand().get(3));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(40, 44, testGameInit.getGame().getPlayers().get(0).getHand().get(4));
        } catch (Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException ignored) {
        }

        int[] anglesCounter = testBoard.getAnglesCounter();

        int score = testGoal.calcScore(testBoard);
        int expectedScore = (anglesCounter[Angles.INK.ordinal()] / 2) * testGoal.getPoints();
        assertEquals(expectedScore, score);
    }

    @Test
    void TripleObjectsCalcScore() {
        ObjectsGoalCard testGoal = new ObjectsGoalCard(new Angles[]{Angles.FEATHER, Angles.INK, Angles.SCROLL}, 3, String.format("Get %s points for each triplet of different objects on the board", 3));

        GameInit testGameInit = new GameInit();
        testGameInit.gameInit();
        Board testBoard = new Board();

        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard, "paolo"));

        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getGoldDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getGoldDeck().poll());

        try {
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(42, 42, testGameInit.getGame().getPlayers().get(0).getHand().get(0));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(43, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(1));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(41, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(2));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(44, 44, testGameInit.getGame().getPlayers().get(0).getHand().get(3));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(40, 44, testGameInit.getGame().getPlayers().get(0).getHand().get(4));
        } catch (Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException ignored) {
        }

        int[] anglesCounter = testBoard.getAnglesCounter();
        int score = testGoal.calcScore(testBoard);
        int scoreCondition = 0;
        while (anglesCounter[Angles.INK.ordinal()] > scoreCondition && anglesCounter[Angles.FEATHER.ordinal()] > scoreCondition && anglesCounter[Angles.SCROLL.ordinal()] > scoreCondition) {
            scoreCondition++;
        }
        int expectedScore = (scoreCondition) * testGoal.getPoints();
        assertEquals(expectedScore, score);
    }

    @Test
    void StepCalcScore() {
        StepGoalCard testGoal = new StepGoalCard(Angles.INSECTS, Angles.PLANTS, false, false, 3, String.format("Get %s points for each regular L-shaped (L) pattern with an %s card at the base and a %s card on the side", 3, Angles.INSECTS, Angles.PLANTS));
        StepGoalCard testGoal2 = new StepGoalCard(Angles.FUNGI, Angles.ANIMALS, true, true, 3, String.format("Get %s points for each regular L-shaped (L) pattern with an %s card at the base and a %s card on the side", 3, Angles.FUNGI, Angles.ANIMALS));
        GameInit testGameInit = new GameInit();
        testGameInit.gameInit();
        Board testBoard = new Board();
        ResourceCard testCard;

        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard, "paolo"));
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());

        ResourceFront testFront = new ResourceFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.ANIMALS});
        testGameInit.getGame().getPlayers().get(0).setHand(testCard);
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.ANIMALS});
        testGameInit.getGame().getPlayers().get(0).setHand(testCard);
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.PLANTS});
        testGameInit.getGame().getPlayers().get(0).setHand(testCard);
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.INSECTS});
        testGameInit.getGame().getPlayers().get(0).setHand(testCard);
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.PLANTS});
        testGameInit.getGame().getPlayers().get(0).setHand(testCard);
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI});
        testGameInit.getGame().getPlayers().get(0).setHand(testCard);

        try {
            testGameInit.getGame().getPlayers().get(0).getHand().get(0).setFlipped(true);
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(42, 42, testGameInit.getGame().getPlayers().get(0).getHand().get(0));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(43, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(1));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(41, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(2));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(44, 44, testGameInit.getGame().getPlayers().get(0).getHand().get(3));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(45, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(4));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(42, 44, testGameInit.getGame().getPlayers().get(0).getHand().get(5));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(40, 44, testGameInit.getGame().getPlayers().get(0).getHand().get(6));
        } catch (Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException ignored) {
        }

        int score, score2;
        score = testGoal.calcScore(testBoard);
        score2 = testGoal2.calcScore(testBoard);
        assertEquals(3, score);
        assertEquals(3, score2);
        assertEquals(6, score + score2);
    }

    @Test
    void DiagonalCalcScore() {
        DiagonalGoalCard testGoal = new DiagonalGoalCard(Angles.FUNGI, 2, true, String.format("Get %s points for each ascending diagonal pattern of mushroom cards on the board", 2));
        GameInit testGameInit = new GameInit();
        testGameInit.gameInit();
        Board testBoard = new Board();

        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard, "paolo"));

        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());
        ResourceFront testFront = new ResourceFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        ResourceCard testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI});
        testGameInit.getGame().getPlayers().get(0).setHand(testCard);
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI});
        testGameInit.getGame().getPlayers().get(0).setHand(testCard);
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI});
        testGameInit.getGame().getPlayers().get(0).setHand(testCard);

        try {
            testGameInit.getGame().getPlayers().get(0).getHand().get(0).setFlipped(true);
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(42, 42, testGameInit.getGame().getPlayers().get(0).getHand().get(0));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(43, 41, testGameInit.getGame().getPlayers().get(0).getHand().get(1));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(44, 40, testGameInit.getGame().getPlayers().get(0).getHand().get(2));
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(45, 39, testGameInit.getGame().getPlayers().get(0).getHand().get(3));
        } catch (Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException ignored) {
        }

        int score = testGoal.calcScore(testBoard);
        assertEquals(2, score);
    }
}
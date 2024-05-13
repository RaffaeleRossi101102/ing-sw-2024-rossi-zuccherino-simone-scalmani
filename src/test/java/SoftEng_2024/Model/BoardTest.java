package SoftEng_2024.Model;


import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Enums.Angles;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testUpdateBoard(){

        GameController testGameInit = new GameController();
        testGameInit.gameInit();
        Board testBoard = new Board();

        try {
            testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard,"paolo"));

            testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());
            testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
            testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
            testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getGoldDeck().poll());

            boolean placed = testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(42, 42, testGameInit.getGame().getPlayers().get(0).getHand().get(0));
            assertTrue(placed);
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(43, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(1));
            assertTrue(placed);
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(41, 43, testGameInit.getGame().getPlayers().get(0).getHand().get(2));
            assertTrue(placed);
            testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(44, 44, testGameInit.getGame().getPlayers().get(0).getHand().get(3));
            assertTrue(placed);

        } catch (Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException ignored) {}
    }

    @Test
    void testUpdateCounters(){
        System.out.println("\n");
        int[] counters;
        int[] tempCounters;
        Angles[] tempAngles;
        GameController testGameInit = new GameController();
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

            counters = testGameInit.getGame().getPlayers().get(0).getHand().get(0).getResource();
            tempCounters = testGameInit.getGame().getPlayers().get(0).getHand().get(1).getResource();
            for (int i=0; i<7; i++){
                counters[i] = counters[i] + tempCounters[i];
            }
            tempCounters = testGameInit.getGame().getPlayers().get(0).getHand().get(2).getResource();
            for (int i=0; i<7; i++){
                counters[i] = counters[i] + tempCounters[i];
            }
            tempCounters = testGameInit.getGame().getPlayers().get(0).getHand().get(3).getResource();
            for (int i=0; i<7; i++){
                counters[i] = counters[i] + tempCounters[i];
            }

            tempAngles = testGameInit.getGame().getPlayers().get(0).getHand().get(0).getFront().getFrontAngles();
            if (Angles.getIndex(tempAngles[1]) != 8 && Angles.getIndex(tempAngles[1]) != 7) counters[Angles.getIndex(tempAngles[1])]--;
            if (Angles.getIndex(tempAngles[3]) != 8 && Angles.getIndex(tempAngles[3]) != 7) counters[Angles.getIndex(tempAngles[3])]--;
            tempAngles = testGameInit.getGame().getPlayers().get(0).getHand().get(1).getFront().getFrontAngles();
            if (Angles.getIndex(tempAngles[3]) != 8 && Angles.getIndex(tempAngles[3]) != 7) counters[Angles.getIndex(tempAngles[3])]--;
            assertArrayEquals(testGameInit.getGame().getPlayers().get(0).getPlayerBoard().getAnglesCounter(), counters);
        } catch (Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException ignored) {}
    }
}
package SoftEng_2024;


import SoftEng_2024.Controller.GameInit;
import SoftEng_2024.Model.Board;
import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testUpdateBoard(){

//        Board testBoard = new Board();
//        List<Card> testResDeck = new ArrayList<>();
//        resourceCardDeserialize(testResDeck);
//        Angles[] testStartingAngles = new Angles[]{Angles.EMPTY, Angles.PLANTS, Angles.INSECTS, Angles.EMPTY, Angles.INSECTS, Angles.EMPTY, Angles.EMPTY};
//        Angles[] testStartingAnglesBack = new Angles[]{Angles.FUNGI, Angles.PLANTS, Angles.INSECTS, Angles.ANIMALS};
//        boolean[] covered = new boolean[4];
//        Arrays.fill(covered, false);
//        ResourceFront testStFront = new ResourceFront(testStartingAngles, 0, covered);
//        StarterCard testStCard = new StarterCard(testStFront, false, testStartingAnglesBack);

//        Player testPL1 = new Player(null, null, "Paolo");
//        Player testPL2 = new Player(null, null, "Francesco");
//
//        Game testGame = new Game()
//


        GameInit testGameInit = new GameInit();
        testGameInit.gameInit();
        Board testBoard = new Board();

        try {
            testGameInit.getGame().getPlayers().add(new Player(new ArrayList<Card>(), testBoard,"paolo"));

            testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());
            testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
            testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getResourceDeck().poll());
            testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getGoldDeck().poll());

            boolean placed = testGameInit.getGame().getPlayers().get(0).getPlayerBoard().updateBoard
                    (42, 42, testGameInit.getGame().getPlayers().get(0).getHand().get(0));
            assertTrue(placed);
        } catch (Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException e) {
            System.out.println("exception met");
        }
    }

    @Test
    void testUpdateCounters() {

    }
}
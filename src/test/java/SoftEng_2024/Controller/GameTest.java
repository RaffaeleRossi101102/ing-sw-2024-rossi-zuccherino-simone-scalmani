package SoftEng_2024.Controller;

import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Player_and_Board.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void turnStart() {
        GameController testGameInit = new GameController();
        testGameInit.gameInit();
        Board testBoard = new Board();

        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard, "paolo"));
        testGameInit.getGame().getPlayers().get(0).setOnline(true);
        testGameInit.getGame().turnStart();

        //Player is online, entering if statement
        assertEquals(testGameInit.getGame().getPlayers().get(0), testGameInit.getGame().getCurrentPlayer());
        assertTrue(testGameInit.getGame().getCurrentPlayer().isPlaying);

        // Player is offline, entering else statement
        testGameInit.getGame().getPlayers().get(0).setOnline(false);
        assertEquals(0, 1%testGameInit.getGame().getPlayers().size());

        // Else case missing
    }


    @Test
    void turnEnd() {
        GameController testGameInit = new GameController();
        testGameInit.gameInit();
        Board testBoard = new Board();

        Player player1 = new Player(new ArrayList<>(), testBoard);
        player1.setNickname("paolo",0);
        testGameInit.getGame().getPlayers().add(player1);
        assertFalse(testGameInit.getGame().turnEnd());

        testGameInit.getGame().getPlayers().get(0).getPlayerBoard().setScore(20);
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setOnline(true);
        testGameInit.getGame().turnStart();
        testGameInit.getGame().playCard(testGameInit.getGame().getPlayers().get(0).getHand().get(0), player1, 42, 42);
        assertTrue(testGameInit.getGame().turnEnd());

        // Empty decks case missing - else case missing
    }

    @Test
    void playCard() {
        GameController testGameInit = new GameController();
        testGameInit.gameInit();
        Board testBoard = new Board();
        int playCode;

        Player player1 = new Player(new ArrayList<>(), testBoard, "paolo");
        testGameInit.getGame().getPlayers().add(player1);
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());

        playCode = testGameInit.getGame().playCard(testGameInit.getGame().getPlayers().get(0).getHand().get(0), player1, 42, 42);
        assertEquals(-3, playCode);

        testGameInit.getGame().getPlayers().get(0).setOnline(true);
        testGameInit.getGame().turnStart();

        playCode = testGameInit.getGame().playCard(testGameInit.getGame().getPlayers().get(0).getHand().get(0), player1, 42, 42);
        assertEquals(1, playCode);

        testGameInit.getGame().turnStart();
        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());
        playCode = testGameInit.getGame().playCard(testGameInit.getGame().getPlayers().get(0).getHand().get(0), player1, 0, 0);
        assertEquals(-1, playCode);

        // necessaryResourcesNotAvailableException case not done yet
    }

    @Test
    void drawPublicCards(){
        GameController testGameInit = new GameController();
        testGameInit.gameInit();
        Board testBoard = new Board();
        int actual;
        int i;

        Player player1 = new Player(new ArrayList<>(), testBoard);
        player1.setNickname("paolo",0);
        testGameInit.getGame().getPlayers().add(player1);

        actual = testGameInit.getGame().drawPublicCards(testGameInit.getGame().getPlayers().get(0), 0);
        assertEquals(-2, actual);

        testGameInit.getGame().getPlayers().get(0).setHand(testGameInit.getGame().getStarterDeck().poll());
        testGameInit.getGame().getPlayers().get(0).setOnline(true);
        testGameInit.getGame().turnStart();

        testGameInit.getGame().getPlayers().get(0).getHand().get(0).setFlipped(true);
        testGameInit.getGame().playCard(testGameInit.getGame().getPlayers().get(0).getHand().get(0), player1, 42, 42);

        actual = testGameInit.getGame().drawPublicCards(player1, 0);
        assertEquals(1, actual);

        testGameInit.getGame().turnStart();
        testGameInit.getGame().getPlayers().get(0).getHand().get(0).setFlipped(true);
        testGameInit.getGame().playCard(testGameInit.getGame().getPlayers().get(0).getHand().get(0), player1, 43, 43);

        for(i=1; i<4; i++) {
            actual = testGameInit.getGame().drawPublicCards(player1, i);
            assertEquals(1, actual);
            testGameInit.getGame().turnStart();
            testGameInit.getGame().getPlayers().get(0).getHand().get(0).setFlipped(true);
            testGameInit.getGame().playCard(testGameInit.getGame().getPlayers().get(0).getHand().get(0), player1, i + 43, i + 43);
        }

        testGameInit.getGame().getPublicCards().clear();
        actual = testGameInit.getGame().drawPublicCards(player1, 0);
        assertEquals(-1, actual);

    }

    @Test
    void drawFromTheDeckTest() {
        GameController testGameInit = new GameController();
        testGameInit.gameInit();
        Board testBoard = new Board();
        int actual;

        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard, "paolo"));
        testGameInit.getGame().getPlayers().get(0).setOnline(true);
        testGameInit.getGame().turnStart();

        int i;
        for (i=0; i<38; i++) {
            actual = testGameInit.getGame().drawFromTheDeck(testGameInit.getGame().getPlayers().get(0), 0);
            assertEquals(1, actual);
        }

        for (i=0; i<38; i++) {
            actual = testGameInit.getGame().drawFromTheDeck(testGameInit.getGame().getPlayers().get(0), 1);
            assertEquals(1, actual);
        }

        actual = testGameInit.getGame().drawFromTheDeck(testGameInit.getGame().getPlayers().get(0), 0);
        assertEquals(-2, actual);

        actual = testGameInit.getGame().drawFromTheDeck(testGameInit.getGame().getPlayers().get(0), 1);
        assertEquals(-3, actual);

        // Missing draw = false case

    }

    @Test
    void shufflePlayers() {
        GameController testGameInit = new GameController();
        testGameInit.gameInit();
        Board testBoard = new Board();

        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        testGameInit.getGame().getPlayers().get(testGameInit.getGame().getPlayers().size()).setNickname("paolo",0);
        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        testGameInit.getGame().getPlayers().get(testGameInit.getGame().getPlayers().size()).setNickname("raffo",0);
        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        testGameInit.getGame().getPlayers().get(testGameInit.getGame().getPlayers().size()).setNickname("fra",0);
        testGameInit.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        testGameInit.getGame().getPlayers().get(testGameInit.getGame().getPlayers().size()).setNickname("giuse",0);

        testGameInit.getGame().shufflePlayers();
        assertEquals(4, testGameInit.getGame().getPlayers().size());
        List<Player> originalList = new ArrayList<>(testGameInit.getGame().getPlayers());

        assertNotEquals(originalList, testGameInit.getClientPlayers());
    }

    // Getters and setters to test
}
package SoftEng_2024.Model.Enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void nextState() {
        assertEquals(GameState.STARTER, GameState.CONNECTION.nextState());
        assertEquals(GameState.SETCOLOR, GameState.STARTER.nextState());
        assertEquals(GameState.CHOOSEGOAL, GameState.SETCOLOR.nextState());
        assertEquals(GameState.PLAY, GameState.CHOOSEGOAL.nextState());
        assertEquals(GameState.NOTPLAYING, GameState.PLAY.nextState());
        assertEquals(GameState.ENDGAME, GameState.NOTPLAYING.nextState());
    }
}
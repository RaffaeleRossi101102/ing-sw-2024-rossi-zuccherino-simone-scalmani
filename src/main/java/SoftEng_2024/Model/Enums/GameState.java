package SoftEng_2024.Model.Enums;

/**
 * Enumeration representing the possible states of a game.
 * <p>
 * The game states included are:
 * - {@code CONNECTION}: Initial state where players connect to the game.
 * - {@code STARTER}: State for setting up the game, initializing players and resources.
 * - {@code SETCOLOR}: State for setting colors or configurations.
 * - {@code CHOOSEGOAL}: State where players choose game goals or objectives.
 * - {@code PLAY}: State where gameplay occurs.
 * - {@code NOTPLAYING}: State indicating that the game is paused or not currently active.
 * - {@code ENDGAME}: State indicating the end of the game.
 */
public enum GameState {
    /**
     * Initial state where players connect to the game.
     */
    CONNECTION,

    /**
     * State for setting up the game, initializing players and resources.
     */
    STARTER,

    /**
     * State for setting colors or configurations.
     */
    SETCOLOR,

    /**
     * State where players choose game goals or objectives.
     */
    CHOOSEGOAL,

    /**
     * State where gameplay occurs.
     */
    PLAY,

    /**
     * State indicating that the game is paused or not currently active.
     */
    NOTPLAYING,

    /**
     * State indicating the end of the game.
     */
    ENDGAME;

    /**
     * Returns the next state in the enum order.
     *
     * @return the next {@code GameState} in the sequence; returns {@code null} if the current state is the last one
     */
    public GameState nextState(){
        return values()[this.ordinal()+1];
    }

}

package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;
/**
 * Represents a message that instructs the GameController to rejoin a game with a specified nickname.
 */
public class ReJoinMessage implements ViewMessage {

    double ID;
    String nickname;
    /**
     * Constructs a ReJoinMessage with the specified player ID and nickname.
     *
     * @param ID       The ID of the player rejoining the game.
     * @param nickname The nickname of the player rejoining the game.
     */
    public ReJoinMessage(double ID, String nickname) {
        this.ID = ID;
        this.nickname = nickname;
    }
    /**
     * Executes the operation to rejoin a game with the specified nickname on the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        controller.reJoinGame(nickname, ID);
    }
}

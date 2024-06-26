package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class RemovedPlayerMessage extends ModelMessage{

    /**
     * The nickname of the player who has been removed.
     */
    String removedPlayerNickname;

    /**
     * Constructs a RemovedPlayerMessage with a message indicating the removal of a player
     * and the nickname of the removed player.
     *
     * @param message The content of the message indicating the player removal.
     * @param removedPlayerNickname The nickname of the player who has been removed.
     */
    public RemovedPlayerMessage( String message,String removedPlayerNickname) {
        super(0, message,"game");
        this.removedPlayerNickname=removedPlayerNickname;
    }

    /**
     * Executes the player removal message by removing the specified player's nickname
     * from the list of players in the local model of the specified view.
     *
     * @param view The view on which to execute the player removal message.
     */
    @Override
    public void executeMessage(View view) {
        view.getLocalModel().getPlayersNickname().remove(removedPlayerNickname);
    }
}

package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;
/**
 * Represents an updated message indicating the online status of a player to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedIsOnlineMessage extends ModelMessage{

    boolean isPlayerOnline;

    /**
     * Constructs an UpdatedIsOnlineMessage with the specified ID, message, online status, and sender's nickname.
     *
     * @param ID The ID associated with the message.
     * @param message The message content.
     * @param isPlayerOnline The online status of the player (true if online, false if offline).
     * @param senderNickname The nickname of the sender.
     */
    public UpdatedIsOnlineMessage(double ID, String message,boolean isPlayerOnline,String senderNickname) {
        super(ID, message,senderNickname);
        this.isPlayerOnline=isPlayerOnline;
    }

    /**
     * Executes the updated is online message on the provided view.
     * Updates the local model's online status of the player based on conditions.
     *
     * @param view The view on which to execute the updated is online message.
     */
    @Override
    public void executeMessage(View view) {
        if (!rejoining | (rejoining & view.getLocalModel().getPlayersNickname().get(senderNickname) == null)) {
            view.getLocalModel().setIfPlayerOnline(senderNickname, isPlayerOnline);
            System.err.println(message);
        }

    }
}

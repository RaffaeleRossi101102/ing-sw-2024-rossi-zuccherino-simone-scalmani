package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;
/**
 * Represents an updated message for a nickname change to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedNicknameMessage extends ModelMessage{

    boolean isItMyNick;
    String nickname;

    /**
     * Constructs an UpdatedNicknameMessage with the specified ID, message, nickname, and indicator if it's the local nickname.
     *
     * @param ID The ID associated with the message.
     * @param message The message content.
     * @param nickname The new nickname.
     * @param isItMyNick Indicates if the nickname change is for the local user (true) or other players (false).
     */
    public UpdatedNicknameMessage(double ID, String message,String nickname,boolean isItMyNick) {
        super(ID, message,nickname);
        this.nickname=nickname;
        this.isItMyNick=isItMyNick;
    }

    /**
     * Executes the updated nickname message on the provided view.
     * Updates the local model's nickname or players' nicknames based on conditions.
     *
     * @param view The view on which to execute the updated nickname message.
     */
    @Override
    public void executeMessage(View view) {
        if(isItMyNick)
            view.getLocalModel().setNickname(nickname);
        else
            view.getLocalModel().setPlayersNickname(nickname);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}

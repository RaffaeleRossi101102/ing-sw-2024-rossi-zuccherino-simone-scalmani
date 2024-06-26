package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.rmi.RemoteException;
/**
 * Represents a message that instructs the GameController to send a whisper message to a player.
 */
public class WhisperMessage implements ViewMessage{
    String message;
    String nickname;
    double ID;
    /**
     * Constructs a WhisperMessage with the specified message, nickname, and player ID.
     *
     * @param message  The message content to be whispered.
     * @param nickname The nickname of the player receiving the whisper message.
     * @param ID       The ID of the player sending the whisper message.
     */
    public WhisperMessage(String message, String nickname, double ID){
        this.message=message;
        this.nickname=nickname;
        this.ID=ID;
    }
    /**
     * Executes the operation to send a whisper message to a player using the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        controller.whisper(nickname,message,ID);
    }
}

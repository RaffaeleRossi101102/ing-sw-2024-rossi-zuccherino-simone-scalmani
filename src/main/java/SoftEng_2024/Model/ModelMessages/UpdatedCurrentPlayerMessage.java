package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class UpdatedCurrentPlayerMessage extends ModelMessage{
    String currentPlayerNickname;
    public UpdatedCurrentPlayerMessage( String message, String currentPlayerNickname) {
        super(0, message, "game");
        this.currentPlayerNickname=currentPlayerNickname;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setCurrentTurnPlayerNickname(currentPlayerNickname);
    }
}

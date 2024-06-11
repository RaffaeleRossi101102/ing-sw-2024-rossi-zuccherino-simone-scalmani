package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.View;

import java.util.List;

public class UpdatedWinnersMessage extends ModelMessage{
    List<String> winnersNickname;
    public UpdatedWinnersMessage( String message,List<String> winnersNickname) {
        super(0, message, "game");
        this.winnersNickname=winnersNickname;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setWinnersNickname(winnersNickname);
        view.getLocalModel().setGameState(GameState.ENDGAME);
    }
}

package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.View;

public class UpdatedGameStateMessage extends ModelMessage{
    GameState gameState;
    public UpdatedGameStateMessage( String message, GameState gameState) {
        super(0, message, "game");
        this.gameState=gameState;
    }

    @Override
    public void executeMessage(View view) {
        if(!rejoining)
            view.getLocalModel().setGameState(gameState);
        else if (view.getLocalModel().getGameState()==null) {
            view.getLocalModel().setGameState(gameState);
        }
    }
}

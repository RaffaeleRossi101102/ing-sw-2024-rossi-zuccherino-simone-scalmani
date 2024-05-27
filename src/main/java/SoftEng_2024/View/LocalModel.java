package SoftEng_2024.View;

import SoftEng_2024.Model.Enums.GameState;

public class LocalModel {
    private GameState gameState;

    public GameState getState(){

        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}

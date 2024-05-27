package SoftEng_2024.View;

import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.GoalCard.GoalCard;

import java.util.HashMap;
import java.util.List;

public class LocalModel {
    private HashMap<String,Color> playersColor;
    private StarterCard starterCard;
    private GameState gameState;
    private List<GoalCard> availableGoals;
    //GETTERS******************************************************************
    public GameState getState(){

        return gameState;
    }

    public List<GoalCard> getAvailableGoals() {
        return availableGoals;
    }

    public StarterCard getStarterCard() {
        return starterCard;
    }

    public HashMap<String, Color> getPlayersColor() {
        return playersColor;
    }


    //SETTERS******************************************************************
    public void setAvailableGoals(List<GoalCard> availableGoals) {
        this.availableGoals = availableGoals;
    }

    public void setPlayersColor(HashMap<String, Color> playersColor) {
        this.playersColor = playersColor;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }
}

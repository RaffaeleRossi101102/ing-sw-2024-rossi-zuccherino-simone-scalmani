package SoftEng_2024.View;


import SoftEng_2024.Model.Cards.*;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.GoalCard.GoalCard;

import java.util.HashMap;
import java.util.List;

public class LocalModel {
    private List<Card> personalHand;
    private HashMap<String,Color> playersColor;
    private StarterCard starterCard;
    private GameState gameState;
    private GameState playerState;
    private List<GoalCard> availableGoals;
    //GETTERS******************************************************************
    public GameState getState(){return gameState;}
    public GameState getPlayerState(){
        return this.playerState;
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

    public List<Card> getPersonalHand() {
        return personalHand;
    }
    //SETTERS******************************************************************

    public void setPersonalHand(List<Card> personalHand) {
        this.personalHand = personalHand;
    }

    public void setAvailableGoals(List<GoalCard> availableGoals) {
        this.availableGoals = availableGoals;
    }

    public void setPlayersColor(HashMap<String, Color> playersColor) {
        this.playersColor = playersColor;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setPlayerState(GameState playerState) {
        this.playerState = playerState;
    }

    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }


}

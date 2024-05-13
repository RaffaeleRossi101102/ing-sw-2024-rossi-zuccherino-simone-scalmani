package SoftEng_2024.View.Messages;

import SoftEng_2024.Controller.GameController;

public class DrawFromTheDeckMessage implements MessageView{
    private int deck;
    private double ID;
    public DrawFromTheDeckMessage(int deck,double ID){
        this.deck=deck;
        this.ID=ID;
    }
    @Override
    public void executeMessage(GameController controller) {
        controller.drawFromTheDeck(deck,ID);
    }
}

package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

public class DrawFromPublicCardsMessage implements ViewMessage {
    private int card;
    private double ID;
    public DrawFromPublicCardsMessage(int card,double ID){
        this.card=card;
        this.ID=ID;
    }
    @Override
    public void executeMessage(GameController controller) {
        controller.drawFromPublicCards(card,ID);
    }
}

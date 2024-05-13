package SoftEng_2024.View.Messages;

import SoftEng_2024.Controller.GameController;

public class DrawFromPublicCardsMessage implements MessageView{
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

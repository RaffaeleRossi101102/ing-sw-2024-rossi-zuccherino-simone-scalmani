package SoftEng_2024.View.Messages;

import SoftEng_2024.Controller.GameController;


public class PlayCardMessage implements MessageView {
    int card;
    int row;
    int column;
    boolean flipped;
    double ID;
    public PlayCardMessage(int card, int row, int column, boolean flipped, double ID){
        this.card = card;
        this.row = row;
        this.column = column;
        this.flipped = flipped;
        this.ID = ID;

    }
    @Override
    public void executeMessage(GameController controller){
        controller.playCard(card, row, column, flipped, ID);
    }
}

package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;
/**
 * Represents a message that instructs the GameController to draw a card from the public cards.
 */
public class DrawFromPublicCardsMessage implements ViewMessage {

    private int card;
    private double ID;
    /**
     * Constructs a DrawFromPublicCardsMessage with the specified card index and player ID.
     *
     * @param card The index of the card to draw from the public cards.
     * @param ID   The ID of the player performing the draw operation.
     */
    public DrawFromPublicCardsMessage(int card,double ID){
        this.card=card;
        this.ID=ID;
    }
    /**
     * Executes the operation to draw a card from the public cards on the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        controller.drawFromPublicCards(card,ID);
    }
}

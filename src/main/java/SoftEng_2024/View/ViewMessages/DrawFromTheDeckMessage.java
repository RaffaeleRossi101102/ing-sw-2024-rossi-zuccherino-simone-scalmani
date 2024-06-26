package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;
/**
 * Represents a message that instructs the GameController to draw a card from a specified deck.
 */
public class DrawFromTheDeckMessage implements ViewMessage {

    private int deck;
    private double ID;
    /**
     * Constructs a DrawFromTheDeckMessage with the specified deck index and player ID.
     *
     * @param deck The index of the deck from which to draw a card.
     * @param ID   The ID of the player performing the draw operation.
     */
    public DrawFromTheDeckMessage(int deck,double ID){
        this.deck=deck;
        this.ID=ID;
    }
    /**
     * Executes the operation to draw a card from the specified deck on the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        controller.drawFromTheDeck(deck,ID);
    }
}

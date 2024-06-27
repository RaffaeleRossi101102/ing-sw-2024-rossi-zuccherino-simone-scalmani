package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

/**
 * Represents a message that instructs the GameController to play a card on the game board.
 */
public class PlayCardMessage implements ViewMessage {
    int card;
    int row;
    int column;
    boolean flipped;
    double ID;
    /**
     * Constructs a PlayCardMessage with the specified parameters.
     *
     * @param card    The index of the card to play.
     * @param row     The row on the game board where the card should be played.
     * @param column  The column on the game board where the card should be played.
     * @param flipped Indicates whether the card should be flipped.
     * @param ID      The ID of the player playing the card.
     */
    public PlayCardMessage(int card, int row, int column, boolean flipped, double ID){
        this.card = card;
        this.row = row;
        this.column = column;
        this.flipped = flipped;
        this.ID = ID;

    }
    /**
     * Executes the operation to play a card on the game board using the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller){
        controller.playCard(card, row, column, flipped, ID);
    }
}

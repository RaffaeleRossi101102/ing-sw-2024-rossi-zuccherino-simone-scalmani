package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;
/**
 * Represents a message used for pong responses in network communication.
 */
public class Pong implements ViewMessage {
    double id;
    double pong;
    /**
     * Constructs a Pong message with the specified IDs.
     *
     * @param id   The ID of the sender.
     * @param pong The pong value.
     */
    public Pong(double id, double pong) {
        this.id = id;
        this.pong = pong;
    }
    /**
     * Gets the pong value associated with this Pong message.
     *
     * @return The pong value.
     */
    public double getPong() {
        return pong;
    }
    /**
     * Executes the message on the GameController.
     *
     * @param controller The GameController instance (not used in this implementation).
     */
    @Override
    public void executeMessage(GameController controller) {
    }
}

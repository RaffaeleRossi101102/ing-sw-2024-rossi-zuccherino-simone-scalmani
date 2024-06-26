package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

import java.io.Serializable;
/**
 * ModelMessage is an abstract class that serves as a base for various types of messages
 * exchanged within the model of a software application.
 * <p>
 * This class implements Serializable, allowing instances to be serialized and sent across
 * network connections or stored in files.
 */
public abstract class ModelMessage implements Serializable {
    double receiverID;
    String message;
    String senderNickname;
    boolean rejoining;

    /**
     * Constructs a ModelMessage with a receiver ID, message content, and sender's nickname.
     *
     * @param ID The receiver's ID to whom the message is intended.
     * @param message The content of the message.
     * @param senderNickname The nickname of the sender of the message.
     */
    public ModelMessage(double ID, String message,String senderNickname) {
        this.receiverID = ID;
        this.message = message;
        this.senderNickname=senderNickname;
        rejoining=false;
    }

    /**
     * Retrieves the sender's nickname associated with this message.
     *
     * @return The sender's nickname.
     */
    public String getSenderNickname() {
        return senderNickname;
    }

    /**
     * Retrieves the receiver's ID associated with this message.
     *
     * @return The receiver's ID.
     */
    public double getReceiverID() {
        return receiverID;
    }

    /**
     * Abstract method to execute the specific action associated with this message on a given view.
     *
     * @param view The view on which to execute the message.
     */
    public abstract void executeMessage(View view);

    /**
     * Sets whether the sender is rejoining the game or not.
     *
     * @param rejoining True if the sender is rejoining, false otherwise.
     */
    public void setRejoining(boolean rejoining){
        this.rejoining=rejoining;
    }

    /**
     * Retrieves whether the sender is rejoining the game.
     *
     * @return True if the sender is rejoining, false otherwise.
     */
    public boolean getRejoining(){
        return this.rejoining;
    }

    /**
     * Sets the receiver's ID to whom the message is intended.
     *
     * @param receiverID The receiver's ID.
     */
    public void setReceiverID(double receiverID) {
        this.receiverID = receiverID;
    }

    /**
     * Sets the sender's nickname associated with this message.
     *
     * @param senderNickname The sender's nickname.
     */
    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }
}

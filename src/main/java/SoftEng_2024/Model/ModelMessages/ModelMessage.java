package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

import java.io.Serializable;

public abstract class ModelMessage implements Serializable {
    double receiverID;
    String message;
    String senderNickname;
    public ModelMessage(double ID, String message,String senderNickname) {
        this.receiverID = ID;
        this.message = message;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public double getReceiverID() {
        return receiverID;
    }

    public abstract void executeMessage(View view);
    //TODO: set ack!!!!

}

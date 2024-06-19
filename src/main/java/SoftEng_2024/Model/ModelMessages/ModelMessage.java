package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

import java.io.Serializable;

public abstract class ModelMessage implements Serializable {
    double receiverID;
    String message;
    String senderNickname;
    boolean rejoining;
    public ModelMessage(double ID, String message,String senderNickname) {
        this.receiverID = ID;
        this.message = message;
        this.senderNickname=senderNickname;
        rejoining=false;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public double getReceiverID() {
        return receiverID;
    }

    public abstract void executeMessage(View view);

    public void setRejoining(boolean rejoining){
        this.rejoining=rejoining;
    }
    public boolean getRejoining(){
        return this.rejoining;
    }

    public void setReceiverID(double receiverID) {
        this.receiverID = receiverID;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }
}

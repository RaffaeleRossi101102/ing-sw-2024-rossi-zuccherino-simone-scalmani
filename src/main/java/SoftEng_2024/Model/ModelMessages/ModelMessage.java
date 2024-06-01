package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

import java.io.Serializable;

public abstract class ModelMessage implements Serializable {
    double ID;
    String message;
    public ModelMessage(double ID, String message) {
        this.ID = ID;
        this.message = message;
    }

    public abstract void executeMessage(View view);
    //TODO: set ack!!!!

}

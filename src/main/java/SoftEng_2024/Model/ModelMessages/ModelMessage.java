package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public abstract class ModelMessage {
    double ID;
    String message;
    public ModelMessage(double ID, String message) {

        this.ID = ID;
        this.message = message;
    }

    public abstract void executeMessage(View view);
}

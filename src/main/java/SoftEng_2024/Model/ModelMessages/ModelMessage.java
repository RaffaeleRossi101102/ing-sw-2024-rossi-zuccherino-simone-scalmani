package SoftEng_2024.Model.ModelMessages;

public abstract class ModelMessage {
    double ID;
    String message;
    public ModelMessage(double ID, String message) {

        this.ID = ID;
        this.message = message;
    }
}

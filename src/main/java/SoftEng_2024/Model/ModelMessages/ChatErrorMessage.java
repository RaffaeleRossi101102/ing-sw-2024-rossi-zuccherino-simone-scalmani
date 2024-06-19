package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class ChatErrorMessage extends ModelMessage{

    public ChatErrorMessage(double ID, String message) {
        super(ID, message, "controller");
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setChatError(message);
    }
}

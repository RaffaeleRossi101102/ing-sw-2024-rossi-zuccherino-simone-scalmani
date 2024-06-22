package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class LastPlayerStandingMessage extends ModelMessage {

    public LastPlayerStandingMessage(String message) {
        super(0, message, "game");

    }

    @Override
    public void executeMessage(View view) {
        System.err.println(message);
    }
}

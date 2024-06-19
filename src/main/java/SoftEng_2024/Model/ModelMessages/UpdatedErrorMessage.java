package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class UpdatedErrorMessage extends ModelMessage{

    public UpdatedErrorMessage(double ID, String message ) {
        super(ID, message, "game");
    }

    @Override
    public void executeMessage(View view) {
       // System.err.println(message);
        view.getLocalModel().setErrorLog(message);
    }

}

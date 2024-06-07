package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class UpdatedBoardMessage extends ModelMessage{
    public UpdatedBoardMessage(double ID, String message, String senderNickname) {
        super(ID, message, senderNickname);
    }

    @Override
    public void executeMessage(View view) {

    }
}

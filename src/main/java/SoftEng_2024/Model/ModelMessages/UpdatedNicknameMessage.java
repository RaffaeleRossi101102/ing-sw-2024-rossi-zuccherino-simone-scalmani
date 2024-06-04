package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class UpdatedNicknameMessage extends ModelMessage{
    String nickname;
    public UpdatedNicknameMessage(double ID, String message,String nickname) {
        super(ID, message,nickname);
        this.nickname=nickname;
    }

    @Override
    public void executeMessage(View view) {

    }
}

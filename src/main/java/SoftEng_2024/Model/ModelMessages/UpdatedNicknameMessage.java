package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class UpdatedNicknameMessage extends ModelMessage{
    boolean isItMyNick;
    String nickname;
    public UpdatedNicknameMessage(double ID, String message,String nickname,boolean isItMyNick) {
        super(ID, message,nickname);
        this.nickname=nickname;
        this.isItMyNick=isItMyNick;
    }

    @Override
    public void executeMessage(View view) {
        if(isItMyNick)
            view.getLocalModel().setNickname(nickname);
        else
            view.getLocalModel().setPlayersNickname(nickname);
    }
}

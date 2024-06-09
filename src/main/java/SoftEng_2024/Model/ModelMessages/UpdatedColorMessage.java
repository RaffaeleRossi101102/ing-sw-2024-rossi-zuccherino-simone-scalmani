package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.View.View;

import java.util.List;

public class UpdatedColorMessage extends ModelMessage{
    private final Color playerColor;
    public UpdatedColorMessage( String message,Color playerColor,String senderNickname) {
        super(0, message,senderNickname);
        this.playerColor=playerColor;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setPlayersColor(senderNickname,playerColor);
    }
}

package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.View.View;

public class UpdatedColorMessage extends ModelMessage{
    private Color playerColor;
    public UpdatedColorMessage(double ID, String nickname,Color playerColor) {
        super(ID, nickname);
        this.playerColor=playerColor;
    }

    @Override
    public void executeMessage(View view) {
       //view.getLocalModel().setPlayersColor(nickname,playerColor);
    }
}

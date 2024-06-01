package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.View.View;

import java.util.List;

public class UpdatedColorMessage extends ModelMessage{
    private List<Color> playerColor;
    public UpdatedColorMessage(double ID, String message,List<Color> playerColor) {
        super(ID, message);
        this.playerColor=playerColor;
    }

    @Override
    public void executeMessage(View view) {
       //view.getLocalModel().setPlayersColor(nickname,playerColor);
    }
}

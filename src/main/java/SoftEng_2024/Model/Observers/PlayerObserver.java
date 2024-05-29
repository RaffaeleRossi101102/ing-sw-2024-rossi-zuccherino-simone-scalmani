package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.Player_and_Board.Player;

public abstract class PlayerObserver {
    public abstract void lookForPlayerChanges(Player pLayer);
    public abstract void updateServer(ModelMessage msg);
}

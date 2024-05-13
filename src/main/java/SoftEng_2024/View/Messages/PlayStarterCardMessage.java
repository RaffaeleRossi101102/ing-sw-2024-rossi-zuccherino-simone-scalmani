package SoftEng_2024.View.Messages;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Board;

public class PlayStarterCardMessage implements MessageView{
    boolean flipped;
    double ID;
    public PlayStarterCardMessage(boolean flipped, double ID){
        this.flipped=flipped;
        this.ID=ID;
    }
    @Override
    public void executeMessage(GameController controller) {
        controller.playStarterCard(flipped,ID);
    }
}

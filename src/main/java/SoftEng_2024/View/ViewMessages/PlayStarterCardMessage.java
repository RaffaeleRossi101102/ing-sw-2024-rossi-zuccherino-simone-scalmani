package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

public class PlayStarterCardMessage implements ViewMessage {
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

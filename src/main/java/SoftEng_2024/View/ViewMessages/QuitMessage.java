package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

public class QuitMessage implements ViewMessage {
    double ID;

    public QuitMessage(double ID) {
        this.ID = ID;
    }

    @Override
    public void executeMessage(GameController controller) {

    }
}

package SoftEng_2024.View.Messages;

import SoftEng_2024.Controller.GameController;

public class QuitMessage implements MessageView{
    double ID;

    public QuitMessage(double ID) {
        this.ID = ID;
    }

    @Override
    public void executeMessage(GameController controller) {

    }
}

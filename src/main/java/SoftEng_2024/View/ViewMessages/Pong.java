package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

public class Pong implements ViewMessage {
    double id;
    double pong;

    public Pong(double id, double pong) {
        this.id = id;
        this.pong = pong;
    }

    public double getPong() {
        return pong;
    }

    @Override
    public void executeMessage(GameController controller) {
    }
}

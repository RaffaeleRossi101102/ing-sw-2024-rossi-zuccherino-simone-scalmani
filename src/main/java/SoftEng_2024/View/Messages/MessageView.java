package SoftEng_2024.View.Messages;

import SoftEng_2024.Controller.GameController;

import java.io.Serializable;

public interface MessageView extends Serializable {
    void executeMessage(GameController controller);
}

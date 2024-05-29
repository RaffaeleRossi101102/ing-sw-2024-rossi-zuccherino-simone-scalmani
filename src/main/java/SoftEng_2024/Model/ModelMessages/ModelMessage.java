package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public abstract class ModelMessage {
    double ID;
    String nickname;
    public ModelMessage(double ID, String nickname) {

        this.ID = ID;
        this.nickname = nickname;
    }

    public abstract void executeMessage(View view);
}

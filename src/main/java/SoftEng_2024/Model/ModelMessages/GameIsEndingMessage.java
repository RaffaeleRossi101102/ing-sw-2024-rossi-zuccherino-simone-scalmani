package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class GameIsEndingMessage extends ModelMessage{
    public GameIsEndingMessage(String message) {
        super(0,message ,"game");
    }

    @Override
    public void executeMessage(View view) {
        System.out.println(message);
    }
}

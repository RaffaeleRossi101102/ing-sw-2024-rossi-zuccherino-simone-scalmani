package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class WinnerDueToForfeitMessage extends ModelMessage{
    String winnerDueToForfeit;
    public WinnerDueToForfeitMessage( String winnerDueToForfeit) {
        super(0, "", "game");
        this.winnerDueToForfeit=winnerDueToForfeit;
    }

    @Override
    public void executeMessage(View view) {
        System.err.println(winnerDueToForfeit+ " YOU WON THE GAME!!!!!");
        System.exit(0);
    }
}

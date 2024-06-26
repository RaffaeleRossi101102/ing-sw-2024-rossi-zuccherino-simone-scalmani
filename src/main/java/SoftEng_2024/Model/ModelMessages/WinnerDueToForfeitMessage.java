package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

import java.util.Timer;
import java.util.TimerTask;

public class WinnerDueToForfeitMessage extends ModelMessage{
    String winnerDueToForfeit;
    public WinnerDueToForfeitMessage( String winnerDueToForfeit) {
        super(0, "", "game");
        this.winnerDueToForfeit=winnerDueToForfeit;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setLastManStanding(true);
        System.err.println("\n\n" + winnerDueToForfeit + " YOU WON THE GAME!!!!!");
        Timer terminationTimer = new Timer();
        TimerTask terminationTask = new TimerTask() {
            @Override
            public void run() {
                System.exit(0); // Exit the application
            }
        };
        terminationTimer.schedule(terminationTask, 10000);
    }
}

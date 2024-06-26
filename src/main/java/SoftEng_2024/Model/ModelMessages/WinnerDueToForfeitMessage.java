package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;
/**
 * Represents a message declaring a winner due to opponent's forfeit.
 * Extends {@link ModelMessage}.
 */
import java.util.Timer;
import java.util.TimerTask;

public class WinnerDueToForfeitMessage extends ModelMessage{

    String winnerDueToForfeit;

    /**
     * Constructs a WinnerDueToForfeitMessage declaring the winner due to opponent's forfeit.
     *
     * @param winnerDueToForfeit The nickname of the winner due to forfeit.
     */
    public WinnerDueToForfeitMessage( String winnerDueToForfeit) {
        super(0, "", "game");
        this.winnerDueToForfeit=winnerDueToForfeit;
    }

    /**
     * Executes the winner due to forfeit message on the provided view.
     * Prints a message declaring the winner and exits the application.
     *
     * @param view The view on which to execute the winner due to forfeit message.
     */
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

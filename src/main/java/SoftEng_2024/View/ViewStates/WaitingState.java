package SoftEng_2024.View.ViewStates;


import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.LocalModel;


/**
 * Represents a state where the client is waiting for model acknowledgement.
 * Manages transitions between states based on model acknowledgement status.
 */
public class WaitingState {
    private ViewState previousState;
    private ViewState nextState;
    private final LocalModel model;

    private final CliViewClient view;

    /**
     * Constructs a WaitingState object with the specified model and view.
     *
     * @param model The LocalModel instance associated with this state.
     * @param view  The CliViewClient instance associated with this state.
     */
    public WaitingState(LocalModel model, CliViewClient view){
        this.model = model;
        this.view = view;
    }
    /**
     * Displays the state and manages transitions based on model acknowledgment status.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void display() {


        while(!model.isAckReceived());

        model.setAckReceived(false);

        if (!model.isAckSuccessful()) {
            //display last error log
            while (model.getErrorLog().isEmpty());
            System.err.println(model.getErrorLog());
            model.setErrorLog("");
            view.setViewState(previousState);
        } else {
            model.setAckSuccessful(false);
            view.setViewState(nextState);
        }

    }

    /**
     * Sets the next state for transition.
     *
     * @param nextState The ViewState to set as the next state.
     */
    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }
    /**
     * Sets the previous state for transition.
     *
     * @param previousState The ViewState to set as the previous state.
     */
    public void setPreviousState(ViewState previousState) {
        this.previousState = previousState;
    }
}

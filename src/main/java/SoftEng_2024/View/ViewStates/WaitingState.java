package SoftEng_2024.View.ViewStates;


import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.LocalModel;



public class WaitingState {
    private ViewState previousState;
    private ViewState nextState;
    private final LocalModel model;

    private final CliViewClient view;



    public WaitingState(LocalModel model, CliViewClient view){
        this.model = model;
        this.view = view;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void display() {

        System.out.println("Waiting for model's acknowledgement...");
        while(!model.isAckReceived());
       // System.out.println(model.isAckReceived());
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


    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }

    public void setPreviousState(ViewState previousState) {
        this.previousState = previousState;
    }
}

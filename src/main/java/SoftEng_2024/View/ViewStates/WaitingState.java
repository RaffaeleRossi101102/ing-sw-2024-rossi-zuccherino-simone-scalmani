package SoftEng_2024.View.ViewStates;


import SoftEng_2024.View.LocalModel;

import java.util.Timer;


public class WaitingState {
    private ViewState previousState;
    private ViewState nextState;
    private final LocalModel model;



    public WaitingState(LocalModel model){
        this.model = model;
    }

    public void display() {
        System.out.println("Waiting for model's acknowledgement...");
        while(!model.isAckReceived());
        model.setAckReceived(false);
        if (!model.isAckSuccessful()) {
            previousState.display();
        } else {
            //display last error log
            System.out.println(model.getErrorLog().get(model.getErrorLog().size() - 1));
            model.setAckSuccessful(false);
            nextState.display();
        }
    }


    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }

    public void setPreviousState(ViewState previousState) {
        this.previousState = previousState;
    }
}

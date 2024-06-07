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
        Thread newStateDisplayThread;
        System.out.println("Waiting for model's acknowledgement...");
        while(!model.isAckReceived());
        System.out.println(model.isAckReceived());
        model.setAckReceived(false);
        System.out.println(model.isAckReceived());
        System.out.println(model.isAckSuccessful());
        if (!model.isAckSuccessful()) {
            //display last error log
            while (model.getErrorLog().isEmpty());
            System.err.println(model.getErrorLog().get(model.getErrorLog().size() - 1));
            newStateDisplayThread = new Thread(previousState::display);
        } else {
            model.setAckSuccessful(false);
            newStateDisplayThread = new Thread(nextState::display);
        }
        newStateDisplayThread.start();
    }


    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }

    public void setPreviousState(ViewState previousState) {
        this.previousState = previousState;
    }
}

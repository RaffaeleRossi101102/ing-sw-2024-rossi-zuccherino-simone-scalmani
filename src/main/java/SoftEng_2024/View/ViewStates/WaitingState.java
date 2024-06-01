package SoftEng_2024.View.ViewStates;


import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.LocalModel;

import javax.swing.*;

public class WaitingState {
    private ViewState previousState;
    private ViewState nextState;
    private LocalModel model;
    //TODO private Log log;



    public WaitingState(LocalModel model){
        this.model = model;
    }

    public void display() {
        System.out.println("Waiting for model's acknowledgement...");
        while(!model.isAckReceived());
        model.setAckReceived(false);
        if (!model.isAckSuccessful()) {
            //diplayLastLog
            previousState.display();
        } else {
            //displayLastLog
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

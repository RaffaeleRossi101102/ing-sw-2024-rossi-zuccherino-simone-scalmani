package SoftEng_2024.View.ViewStates;


import javax.swing.*;

public class WaitingState  {
    private volatile boolean ackReceived;
    private boolean ackSuccessful;
    private ViewState previousState;
    private ViewState nextState;
    //TODO private Log log;



    public WaitingState(){
        this.ackReceived=false;
    }

    public void display() {
        System.out.println("Waiting for model's acknowledgement...");
        while(!ackReceived);
        if (!ackSuccessful) {
            //diplayLastLog
            previousState.display();
        } else {
            //displayLastLog
            nextState.display();
        }
    }

    public void setAckSuccessful(boolean ackSuccessful) {
        this.ackSuccessful = ackSuccessful;
        this.ackReceived = true;
    }

    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }

    public void setPreviousState(ViewState previousState) {
        this.previousState = previousState;
    }
}

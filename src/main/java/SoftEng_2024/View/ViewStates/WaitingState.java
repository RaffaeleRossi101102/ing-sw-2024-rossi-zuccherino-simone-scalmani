package SoftEng_2024.View.ViewStates;


import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.LocalModel;



public class WaitingState {
    private ViewState previousState;
    private ViewState nextState;
    private final LocalModel model;

    private CliViewClient view;



    public WaitingState(LocalModel model, CliViewClient view){
        this.model = model;
        this.view = view;
    }

    public void display() {

        System.out.println("Waiting for model's acknowledgement...");
        while(!model.isAckReceived());
       // System.out.println(model.isAckReceived());
        model.setAckReceived(false);
        //System.out.println(model.isAckReceived());
        //System.out.println(model.isAckSuccessful());
        if (!model.isAckSuccessful()) {
            //display last error log
            //while (model.getErrorLog().isEmpty());
            //TODO da ricontrollare sequenza messaggi, da valutare coda errorLog ed il take del messaggio per il while di attesa isEmpty sopra
            String error = model.getErrorLog().get(model.getErrorLog().size() - 1);
            System.err.println(error);
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

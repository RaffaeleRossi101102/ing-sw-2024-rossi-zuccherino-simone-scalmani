package SoftEng_2024.View;

import SoftEng_2024.View.ViewStates.ViewState;

public interface View {


    void run();
    double getID();
    LocalModel getLocalModel();
    void setCommand(String command);
    String getCommand();
    ViewState getViewState();


}

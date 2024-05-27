package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;

public class DrawState extends ViewState{
    public DrawState(CliViewClient view, ClientInterface client, double ID) {
        super(view,client,ID);
    }

    @Override
    public void display() {

    }
}

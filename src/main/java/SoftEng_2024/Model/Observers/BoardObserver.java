package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.ModelMessages.UpdatedBoardMessage;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Network.ToView.ObServerManager;

public class BoardObserver {
    private final String callerNickname;
    private final ObServerManager obServerManager;
    public BoardObserver(String callerNickname,ObServerManager o){
        this.callerNickname=callerNickname;
        obServerManager=o;
    }
    public void updatedBoard(Board board){
        notifyServer(new UpdatedBoardMessage(0,"",callerNickname));
    }
//    public void setID(double ID) {
//        this.ID = ID;
//    }
    public void notifyServer(ModelMessage msg){
        obServerManager.addModelMessageToQueue(msg);
    }
}

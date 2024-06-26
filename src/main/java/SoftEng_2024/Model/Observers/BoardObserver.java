package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.ModelMessages.UpdatedBoardMessage;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Network.ToView.ObServerManager;
/**
 * Observer responsible for handling updates related to the game board and notifying the server.
 */
public class BoardObserver {

    private final String callerNickname;
    private final ObServerManager obServerManager;

    /**
     * Constructs a BoardObserver with the caller's nickname and the Observer Manager.
     *
     * @param callerNickname The nickname of the caller associated with this observer.
     * @param o The Observer Manager instance to handle message notifications.
     */
    public BoardObserver(String callerNickname,ObServerManager o){
        this.callerNickname=callerNickname;
        obServerManager=o;
    }

    /**
     * Notifies the server with an updated board message.
     *
     * @param board The updated Board object containing new board state.
     */
    public void updatedBoard(Board board){
        notifyServer(new UpdatedBoardMessage(0,"",callerNickname, board.getCardBoard(),board.getCardList(),board.getAnglesCounter(),board.getScore() ));
    }

    /**
     * Notifies the server by adding a ModelMessage to the server's message queue.
     *
     * @param msg The ModelMessage to be added to the server's message queue.
     */
    public void notifyServer(ModelMessage msg){
        obServerManager.addModelMessageToQueue(msg);
    }

    /**
     * Retrieves the caller's nickname associated with this message.
     *
     * @return The caller's nickname.
     */
    public String getCallerNickname() {
        return callerNickname;
    }
}

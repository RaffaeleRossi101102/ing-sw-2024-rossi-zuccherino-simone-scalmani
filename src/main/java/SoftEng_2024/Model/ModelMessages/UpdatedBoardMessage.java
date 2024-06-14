package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Player_and_Board.Cell;
import SoftEng_2024.View.View;

import java.util.ArrayList;

public class UpdatedBoardMessage extends ModelMessage{
    private Cell[][] cardBoard;
    private ArrayList<Cell> cardList;
    private int[] anglesCounter;
    private int score;
    public UpdatedBoardMessage(double ID, String message, String senderNickname,Cell[][] cardBoard,ArrayList<Cell> cardList,int[] anglesCounter,int score) {
        super(ID, message, senderNickname);
        this.cardBoard=cardBoard;
        this.cardList=cardList;
        this.anglesCounter=anglesCounter;
        this.score=score;
    }

    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & !view.getLocalModel().getPlayersBoards().containsKey(senderNickname)))
            view.getLocalModel().setLocalBoard(senderNickname,cardBoard,cardList,score,anglesCounter);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}

package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class UpdatedAckMessage extends ModelMessage{
    boolean ack;
    public UpdatedAckMessage(double ID, String message, boolean ack) {
        super(ID, message, "game");
        this.ack=ack;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setAckSuccessful(ack);
        view.getLocalModel().setAckReceived(true);
        System.out.println("sto eseguendo il messaggio con ack=" + ack);

    }
}

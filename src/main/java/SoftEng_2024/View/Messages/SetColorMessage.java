package SoftEng_2024.View.Messages;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Enums.Color;

public class SetColorMessage implements MessageView{
    Color color;
    double ID;
    public SetColorMessage(Color color,double ID){
        this.color=color;
        this.ID=ID;
    }
    @Override
    public void executeMessage(GameController controller){
        controller.setColor(this.color,this.ID);
    }
}

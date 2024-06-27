module SoftEng_2024 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires com.google.gson;
    requires java.desktop;
    requires jdk.javadoc;


    opens SoftEng_2024.View to javafx.fxml;
    opens SoftEng_2024.Model.Cards to com.google.gson;
    opens SoftEng_2024.View.GUIControllers to javafx.fxml;
    exports SoftEng_2024.Network.ToModel;
    exports SoftEng_2024.View to javafx.graphics;
    exports SoftEng_2024.View.GUIControllers to javafx.graphics;
    exports SoftEng_2024.View.ViewMessages;
    exports SoftEng_2024.Model.ModelMessages;
    exports SoftEng_2024.Controller;
    exports SoftEng_2024.Model.Enums;
    exports SoftEng_2024.View.ViewStates;
    exports SoftEng_2024.Model.Player_and_Board;
    exports SoftEng_2024.Model.Cards;
    exports SoftEng_2024.Model.GoalCard;
    exports SoftEng_2024.Network.ToView;
    exports SoftEng_2024.Model;
    //opens to
}
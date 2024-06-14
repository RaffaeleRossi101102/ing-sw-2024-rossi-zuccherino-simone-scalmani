module SoftEng_2024 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires com.google.gson;
    requires java.desktop;
    requires jdk.javadoc;


    opens SoftEng_2024.View to javafx.fxml;
    opens SoftEng_2024.Model.Cards to com.google.gson;
    exports SoftEng_2024.Network.ToModel;
    exports SoftEng_2024.View to javafx.graphics;
    exports SoftEng_2024.View.GUIControllers to javafx.graphics;
    opens SoftEng_2024.View.GUIControllers to javafx.fxml;
}
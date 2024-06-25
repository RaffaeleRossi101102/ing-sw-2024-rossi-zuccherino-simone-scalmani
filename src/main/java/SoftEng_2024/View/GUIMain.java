package SoftEng_2024.View;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.View.GUIControllers.MainViewController;
import SoftEng_2024.View.ViewStates.ViewState;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.*;
import javafx.fxml.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javafx.application.Application.launch;

public class GUIMain extends Application implements View{
    private static double ID;
    private static ClientInterface client;
    private static LocalModel localModel;
    private final Font font = Font.loadFont(getClass().getResourceAsStream("/FrakturNo2.ttf"), 10);
    private final Font font2 = Font.loadFont(getClass().getResourceAsStream("/LTCushion-Black.ttf"), 10);

    public GUIMain() {
    }

    public static void setID(double id) {
        ID = id;
    }

    public static void setClient(ClientInterface clientVal) {
        client = clientVal;
    }

    public static void setLocalModel(LocalModel newLocalModel){
        localModel = newLocalModel;
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(GUIMain.class.getResource("/MainMenu.fxml")));
        Parent root = loader.load();

        MainViewController.setID(ID);
        MainViewController.setClient(client);
        MainViewController.setLocalModel(localModel);

        Scene mainMenu = new Scene(root);
        stage.getIcons().add(new Image(Objects.requireNonNull(GUIMain.class.getResourceAsStream("/icon_cn.png"))));
        stage.setTitle("Codex Naturalis");
        stage.setScene(mainMenu);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public double getID() {
        return ID;
    }

    @Override
    public LocalModel getLocalModel() {
        return localModel;
    }

    @Override
    public void setCommand(String command) {
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public ViewState getViewState() {
        return null;
    }
}
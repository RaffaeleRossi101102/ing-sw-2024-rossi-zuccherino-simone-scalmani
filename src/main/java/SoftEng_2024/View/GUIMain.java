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

/**
 * This class represents the main application for the GUI.
 * It implements the Application interface and serves as the entry point for
 * launching the GUI.
 */
public class GUIMain extends Application implements View{
    private static double ID;
    private static ClientInterface client;
    private static LocalModel localModel;
    private final Font font = Font.loadFont(getClass().getResourceAsStream("/FrakturNo2.ttf"), 10);
    private final Font font2 = Font.loadFont(getClass().getResourceAsStream("/LTCushion-Black.ttf"), 10);

    /**
     * Default constructor for GUIMain.
     * Here to appease JavaDoc.
     */
    public GUIMain() {
    }

    /**
     * Sets the ID of the client.
     *
     * @param id The ID to set.
     */
    public static void setID(double id) {
        ID = id;
    }

    /**
     * Sets the client interface for communication.
     *
     * @param clientVal The client interface to set.
     */
    public static void setClient(ClientInterface clientVal) {
        client = clientVal;
    }

    /**
     * Sets the local model for the GUI application.
     *
     * @param newLocalModel The local model to set.
     */
    public static void setLocalModel(LocalModel newLocalModel){
        localModel = newLocalModel;
    }

    /**
     * Starts the GUI application, loading the main menu.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
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

    /**
     * Launches the GUI application.
     */
    @Override
    public void run() {
        launch();
    }

    /**
     * Retrieves the ID associated with the client.
     *
     * @return The ID of the GUI application.
     */
    @Override
    public double getID() {
        return ID;
    }

    /**
     * Retrieves the local model associated with the GUI application.
     *
     * @return The local model of the GUI application.
     */
    @Override
    public LocalModel getLocalModel() {
        return localModel;
    }

    /**
     * Sets a command for the GUI application.
     * Unused.
     *
     * @param command The command to set.
     */
    @Override
    public void setCommand(String command) {
    }

    /**
     * Retrieves the command associated with the GUI application.
     * Unused.
     *
     * @return The command of the GUI application.
     */
    @Override
    public String getCommand() {
        return null;
    }

    /**
     * Retrieves the view state of the GUI application.
     * Unused.
     *
     * @return The view state of the GUI application.
     */
    @Override
    public ViewState getViewState() {
        return null;
    }
}
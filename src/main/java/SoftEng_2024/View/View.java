package SoftEng_2024.View;

import SoftEng_2024.View.ViewStates.ViewState;
/**
 * Interface representing a generic view for the Codex Naturalis game.
 * Implementations of this interface will provide methods to run the view,
 * retrieve an ID, access the local model, manage commands, and obtain the current view state.
 */
public interface View {

    /**
     * Method to start or run the view.
     */
    void run();

    /**
     * Retrieves the ID associated with this view.
     *
     * @return The ID of the view.
     */
    double getID();

    /**
     * Retrieves the local model associated with this view.
     *
     * @return The local model instance.
     */
    LocalModel getLocalModel();

    /**
     * Sets a command for this view.
     *
     * @param command The command to be set.
     */
    void setCommand(String command);

    /**
     * Retrieves the current command set for this view.
     *
     * @return The current command.
     */
    String getCommand();

    /**
     * Retrieves the current state of the view.
     *
     * @return The current view state.
     */
    ViewState getViewState();


}

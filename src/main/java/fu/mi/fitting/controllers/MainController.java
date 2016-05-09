package fu.mi.fitting.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by shang on 5/6/2016.
 * this is main controller, because this class
 * is the controller of main.fxml.
 * but main.fxml includes many fxml, and they have there own controllers
 * so this controller's responsibility is not as much as its name.
 */
public class MainController {
    @FXML
    Label statusLabel;

    @FXML
    public void initialize() {
        ControllerResource.getInstance().mainController = this;
    }

    /**
     * set message to the status bar,
     *
     * @param charSequence message to show
     */
    public void setStatus(CharSequence charSequence) {
        statusLabel.setText(charSequence.toString());
    }
}

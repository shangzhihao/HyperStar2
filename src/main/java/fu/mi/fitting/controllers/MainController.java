package fu.mi.fitting.controllers;

import javafx.fxml.FXML;

/**
 * Created by shang on 5/1/2016.
 */
public class MainController {
    @FXML
    public void initialize() {
        ControllerResource.getInstance().mainController = this;
    }
}

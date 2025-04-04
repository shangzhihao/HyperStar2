package fu.mi.fitting.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Created by shang on 5/9/2016.
 * Controller of menu.fxml
 */
public class MenuController {
    @FXML
    public void initialize() {
        Controllers.getInstance().menuController = this;
    }

    public void quit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void loadSamples(ActionEvent actionEvent) {
        Controllers.getInstance().sampleController.loadSamplesBtn.fire();
    }
}

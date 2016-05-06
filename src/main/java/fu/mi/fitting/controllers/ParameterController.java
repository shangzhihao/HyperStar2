package fu.mi.fitting.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Created by shang on 5/6/2016.
 */
public class ParameterController {
    ControllerResource cr = ControllerResource.getInstance();

    @FXML
    public void initialize() {
        cr.parameterController = this;
    }

    public void loadSamples(ActionEvent actionEvent) {
        System.out.println("loadSamples");
    }

    public void fitDistribution(ActionEvent actionEvent) {
        System.out.println("fit distribution");
    }
}

package fu.mi.fitting.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

/**
 * Created by shang on 5/6/2016.
 */
public class ChartsController {
    @FXML
    Tab pdfTab;
    @FXML
    AnchorPane pdfPane;
    @FXML
    Tab cdfTab;
    @FXML
    AnchorPane cdfPane;
    @FXML
    Tab momTab;
    @FXML
    AnchorPane momPane;

    @FXML
    public void initialize() {
        ControllerResource.getInstance().chartsController = this;
    }
}

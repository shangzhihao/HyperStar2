package fu.mi.fitting.controllers;

import fu.mi.fitting.distributions.PHDistribution;
import fu.mi.fitting.parameters.Messages;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Optional;

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
    BorderPane mainPane;
    @FXML
    VBox parameters;

    private Property<String> status = new SimpleStringProperty(Messages.NONE_STATUS);
    private Optional<PHDistribution> fitRes = Optional.empty();
    @FXML
    public void initialize() {
        Controllers.getInstance().mainController = this;
        statusLabel.textProperty().bindBidirectional(status);
    }

    /**
     * set message to the status bar,
     *
     * @param charSequence message to show
     */
    public void setStatus(CharSequence charSequence) {
        status.setValue(charSequence.toString());
//        statusLabel.setText(charSequence.toString());
    }

    public void showWarn(CharSequence message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message.toString());
        alert.showAndWait();
    }

    public void setInputDisable(boolean enable) {
        parameters.setDisable(enable);
    }

    public Optional<PHDistribution> getFitRes() {
        return fitRes;
    }

    public void setFitRes(Optional<PHDistribution> fitRes) {
        this.fitRes = fitRes;
    }
}

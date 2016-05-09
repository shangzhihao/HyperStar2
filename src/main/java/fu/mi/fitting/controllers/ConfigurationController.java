package fu.mi.fitting.controllers;

import fu.mi.fitting.fitters.ExponentialFitter;
import fu.mi.fitting.fitters.HyperErlangFitter;
import fu.mi.fitting.fitters.MomErlangFitter;
import fu.mi.fitting.parameters.ChartsParameters;
import fu.mi.fitting.parameters.FitParameters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 * Created by shang on 5/9/2016.
 * controller of the configuration.fxml
 * react to the fit configuration setting.
 */
public class ConfigurationController {
    @FXML
    ChoiceBox<String> fitterChoice;
    @FXML
    TextField binsTxt;
    @FXML
    TextField cdfPointsTxt;
    @FXML
    TextField pdfPointsTxt;

    @FXML
    public void initialize() {
        ControllerResource.getInstance().confController = this;
        fitterChoice.getItems().addAll(HyperErlangFitter.FITTER_NAME,
                ExponentialFitter.FITTER_NAME, MomErlangFitter.FITTER_NAME);
        fitterChoice.setTooltip(new Tooltip("select a distribution to fit"));
        fitterChoice.getSelectionModel().selectFirst();
        addListener();
    }

    private void addListener() {
        // number of histogram bins changed listener
        binsTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int bins = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setBins(bins);
            } catch (RuntimeException e) {
            }
        });
        // number of pdf points changed listener
        pdfPointsTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int bins = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setPdfPoints(bins);
            } catch (RuntimeException e) {
            }
        });
        // number of cdf points changed listener
        cdfPointsTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int bins = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setCdfPoints(bins);
            } catch (RuntimeException e) {
            }
        });
    }

    /**
     * executed when select new fitter
     *
     * @param actionEvent select new item event
     */
    public void chooseFitter(ActionEvent actionEvent) {
        FitParameters.getInstance().setFitterName(fitterChoice.getValue());
    }
}

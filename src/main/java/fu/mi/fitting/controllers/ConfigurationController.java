package fu.mi.fitting.controllers;

import fu.mi.fitting.fitters.ExponentialFitter;
import fu.mi.fitting.fitters.HyperErlangFitter;
import fu.mi.fitting.fitters.MomErlangFitter;
import fu.mi.fitting.parameters.ChartsParameters;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.parameters.Messages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    TextField branchText;
    @FXML
    TextField maxMomentText;


    Logger logger = LoggerFactory.getLogger(this.getClass());

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
        maxMomentText.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int maxMomentOrder = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setMaxMomentOrder(maxMomentOrder);
            } catch (RuntimeException e) {
                logger.warn("{} is not a integer, can't set it as max moment order", newValue);
            }

        });
        // how many branch in hyper-erlang distribuion
        branchText.textProperty().addListener((observable, oldVlue, newValue) -> {
            try {
                int branch = Integer.parseInt(newValue);
                FitParameters.getInstance().setBranch(branch);
            } catch (RuntimeException e) {
                logger.warn("{} is not a integer, can't set it as branch", newValue);
            }
            if (newValue.equals(Messages.AUTO_BRANCH)) {
                FitParameters.getInstance().setBranchToDefault();
            }
        });
        // number of histogram bins changed listener
        binsTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int bins = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setBins(bins);
            } catch (RuntimeException e) {
                logger.warn("{} is not a integer, can't set it as histogram bins", newValue);
            }
        });
        // number of pdf points changed listener
        pdfPointsTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int points = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setPdfPoints(points);
            } catch (RuntimeException e) {
                logger.warn("{} is not a integer, can't set it as pdf points", newValue);
            }
        });
        // number of cdf points changed listener
        cdfPointsTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int points = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setCdfPoints(points);
            } catch (RuntimeException e) {
                logger.warn("{} is not a integer, can't set it as cdf points", newValue);
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

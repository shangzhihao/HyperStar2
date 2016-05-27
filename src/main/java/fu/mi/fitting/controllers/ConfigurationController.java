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
import javafx.scene.layout.GridPane;

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
    @FXML
    ChoiceBox<String> equalityChoice;
    @FXML
    ChoiceBox<String> emptyChoice;
    @FXML
    ChoiceBox<String> terminationChoice;
    @FXML
    GridPane hyperErlangGrid;
    @FXML
    GridPane erlangGrid;
    @FXML
    TextField reassignText;
    @FXML
    TextField optimizeText;
    @FXML
    TextField invoksText;
    @FXML
    TextField phaseText;

    @FXML
    public void initialize() {
        ControllerResource.getInstance().confController = this;
        fitterChoice.getItems().addAll(HyperErlangFitter.FITTER_NAME,
                MomErlangFitter.FITTER_NAME, ExponentialFitter.FITTER_NAME);
        fitterChoice.getSelectionModel().selectFirst();
        equalityChoice.getItems().addAll("ParameterEquals", "RegularEquals", "ReferenceEquals");
        equalityChoice.getSelectionModel().selectFirst();
        emptyChoice.getItems().addAll("RemoveCluster", "KeepEmptyCluster");
        emptyChoice.getSelectionModel().selectFirst();
        terminationChoice.getItems().addAll("ClusterEquality", "MinLLogikelihoodReached");
        terminationChoice.getSelectionModel().selectFirst();
        addListener();
    }

    private void addListener() {
        //
        maxMomentText.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int maxMomentOrder = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setMaxMomentOrder(maxMomentOrder);
            } catch (RuntimeException e) {
                //logger.warn("{} is not a integer, can't set it as max moment order", newValue);
            }

        });
        // how many branch in hyper-erlang distribuion
        branchText.textProperty().addListener((observable, oldVlue, newValue) -> {
            try {
                int branch = Integer.parseInt(newValue);
                FitParameters.getInstance().setBranch(branch);
            } catch (RuntimeException e) {
                //logger.warn("{} is not a integer, can't set it as branch", newValue);
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
                //logger.warn("{} is not a integer, can't set it as histogram bins", newValue);
            }
        });
        // number of pdf points changed listener
        pdfPointsTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int points = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setPdfPoints(points);
            } catch (RuntimeException e) {
                //logger.warn("{} is not a integer, can't set it as pdf points", newValue);
            }
        });
        // number of cdf points changed listener
        cdfPointsTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int points = Integer.parseInt(newValue);
                ChartsParameters.getInstance().setCdfPoints(points);
            } catch (RuntimeException e) {
                //logger.warn("{} is not a integer, can't set it as cdf points", newValue);
            }
        });
        // fitter change listener
        fitterChoice.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            hyperErlangGrid.setVisible(false);
            erlangGrid.setVisible(false);
            int selected = newValue.intValue();
            switch (selected) {
                case 0:
                    hyperErlangGrid.setVisible(true);
                    break;
                case 1:
                    erlangGrid.setVisible(true);
                    break;
                case 2:
                    break;
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

    public void setInputDisable(boolean isDisable) {
        fitterChoice.setDisable(isDisable);
        binsTxt.setDisable(isDisable);
        cdfPointsTxt.setDisable(isDisable);
        pdfPointsTxt.setDisable(isDisable);
        branchText.setDisable(isDisable);
        maxMomentText.setDisable(isDisable);
        equalityChoice.setDisable(isDisable);
        emptyChoice.setDisable(isDisable);
        terminationChoice.setDisable(isDisable);
    }
}

package fu.mi.fitting.controllers;

import fu.mi.fitting.fitters.ExponentialFitter;
import fu.mi.fitting.fitters.HyperErlangFitter;
import fu.mi.fitting.fitters.MapFitter;
import fu.mi.fitting.fitters.MomErlangFitter;
import fu.mi.fitting.parameters.ChartsParameters;
import fu.mi.fitting.parameters.FitParameters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

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
    TextField shuffleText;

    @FXML
    TextField phaseText;
    Map<Integer, GridPane> indexToGrid = newHashMap();

    @FXML
    public void initialize() {
        Controllers.getInstance().confController = this;
        fitterChoice.getItems().addAll(HyperErlangFitter.FITTER_NAME,
                MomErlangFitter.FITTER_NAME, ExponentialFitter.FITTER_NAME,
                MapFitter.FITTER_NAME);
        fitterChoice.getSelectionModel().selectFirst();
        equalityChoice.getItems().addAll("ParameterEquals", "RegularEquals", "ReferenceEquals");
        equalityChoice.getSelectionModel().selectFirst();
        emptyChoice.getItems().addAll("RemoveCluster", "KeepEmptyCluster");
        emptyChoice.getSelectionModel().selectFirst();
        terminationChoice.getItems().addAll("ClusterEquality", "MinLLogikelihoodReached");
        terminationChoice.getSelectionModel().selectFirst();

        indexToGrid.put(0, hyperErlangGrid);
        indexToGrid.put(1, erlangGrid);
        indexToGrid.put(3, hyperErlangGrid);

        bindProperty();
    }

    private void bindProperty() {
        FitParameters fitParameters = FitParameters.getInstance();
        ChartsParameters chartsParameters = ChartsParameters.getInstance();
        // how many branch in hyper-erlang distribuion
        branchText.textProperty().bindBidirectional(fitParameters.getBranchProperty());
        reassignText.textProperty().bindBidirectional(fitParameters.getReassignProperty());
        optimizeText.textProperty().bindBidirectional(fitParameters.getOptimizeProperty());
        shuffleText.textProperty().bindBidirectional(fitParameters.getShuffleProperty());

        cdfPointsTxt.textProperty().bindBidirectional(chartsParameters.getCDFProperty());
        pdfPointsTxt.textProperty().bindBidirectional(chartsParameters.getPDFProperty());
        binsTxt.textProperty().bindBidirectional(chartsParameters.getBinsProperty());
        maxMomentText.textProperty().bindBidirectional(chartsParameters.getMomentsProperty());

        fitterChoice.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            indexToGrid.values().stream().forEach(grid -> grid.setVisible(false));
            int selected = newValue.intValue();
            if (indexToGrid.containsKey(selected)) {
                indexToGrid.get(selected).setVisible(true);
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

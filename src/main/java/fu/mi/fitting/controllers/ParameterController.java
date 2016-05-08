package fu.mi.fitting.controllers;

import fu.mi.fitting.fitters.*;
import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.io.SampleReader;
import fu.mi.fitting.parameters.ChartsParameters;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.jfree.data.function.Function2D;

import java.io.File;

/**
 * Created by shang on 5/6/2016.
 * this controller reacts to the user input form the
 * right side bar, most actions are controlled here.
 */
public class ParameterController {

    private final ControllerResource controllerResource = ControllerResource.getInstance();
    private final FitParameters fitParameters = FitParameters.getInstance();
    @FXML
    ChoiceBox<String> fitterChoice;
    @FXML
    Button fitBtn;
    @FXML
    TextField binsTxt;
    @FXML
    TextField cdfPointsTxt;
    @FXML
    TextField pdfPointsTxt;

    @FXML
    public void initialize() {
        controllerResource.parameterController = this;
        fitterChoice.getItems().addAll(HyperErlangFitter.DISPLAY_NAME,
                ExponentialFitter.DISPLAY_NAME, MomErlangFitter.DISPLAY_NAME);
        fitterChoice.setTooltip(new Tooltip("select a distribution to fit"));
        fitterChoice.getSelectionModel().selectFirst();
    }

    /**
     * execute when load sample button is clicked.
     * read samples from selected file
     *
     * @param actionEvent click event
     */
    public void loadSamples(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File inputFile = fileChooser.showOpenDialog(controllerResource.stage);
        if (inputFile == null) {
            return;
        }
        SampleReader sr = new LineSampleReader(inputFile);
        controllerResource.mainController.setSampleCollection(sr.read());
        controllerResource.chartsController.drawChart();
    }

    /**
     * executed when fit button is clicked
     *
     * @param actionEvent click event
     */
    public void fitBtnAction(ActionEvent actionEvent) {
        fitBtn.setDisable(true);
        fitDistribution();
        fitBtn.setDisable(false);
    }

    /**
     * fit distribution with selected fitter and parameters
     */
    private void fitDistribution() {
        SampleCollection sc = controllerResource.mainController.getSampleCollection();
        if (sc == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setContentText("Please load samples first.");
            alert.showAndWait();
            return;
        }
        Fitter herFitter = FitterFactory.getFitterByName(fitParameters.getFitterName(), sc);
        RealDistribution res = herFitter.fit();
        Function2D pdf = d -> res.density(d);
        double start = StatUtils.min(sc.asDoubleArray());
        double end = StatUtils.max(sc.asDoubleArray());
        controllerResource.chartsController.addPDF(pdf, start, end);
    }

    /**
     * executed when select new fitter
     *
     * @param actionEvent select new item event
     */
    public void chooseFitter(ActionEvent actionEvent) {
        fitParameters.setFitterName(fitterChoice.getValue());
    }

    public void changeBins(Event event) {
        try {
            int bins = Integer.parseInt(binsTxt.getText());
            ChartsParameters.getInstance().setBins(bins);
        } catch (RuntimeException e) {
        }
    }

    public void changePDFPoints(Event event) {
        try {
            int pdfPoints = Integer.parseInt(pdfPointsTxt.getText());
            ChartsParameters.getInstance().setPdfPoints(pdfPoints);
        } catch (RuntimeException e) {
        }
    }

    public void changeCDFPoint(Event event) {
        try {
            int cdfPoints = Integer.parseInt(cdfPointsTxt.getText());
            ChartsParameters.getInstance().setCdfPoints(cdfPoints);
        } catch (RuntimeException e) {
        }
    }
}

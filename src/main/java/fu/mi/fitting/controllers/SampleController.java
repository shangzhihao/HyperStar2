package fu.mi.fitting.controllers;

import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.io.SampleReader;
import fu.mi.fitting.parameters.Messages;
import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by shang on 5/9/2016.
 * controller of samples.fxml
 * reacts to actions about samples.
 */
public class SampleController {
    @FXML
    Button loadSamplesBtn;
    @FXML
    Slider sampleSizeSlider;
    @FXML
    TextField sampleRangeFrom;
    @FXML
    TextField sampleRangeTo;
    @FXML
    Label sampleSizeText;
    @FXML
    Button rePlotBtn;

    @FXML
    public void initialize() {
        Controllers.getInstance().sampleController = this;
        bindProperty();
    }

    /**
     * add listeners to value changed event
     */
    private void bindProperty() {
        SamplesParameters parameters = SamplesParameters.getInstance();
        sampleSizeSlider.valueProperty().bindBidirectional(parameters.getSizeProperty());
        sampleRangeFrom.textProperty().bindBidirectional(parameters.getFromProperty());
        sampleRangeTo.textProperty().bindBidirectional(parameters.getToProperty());
    }

    /**
     * execute when load sample button is clicked.
     * read samples from selected file
     *
     * @param actionEvent click event
     */
    public void loadSamples(ActionEvent actionEvent) {
        MainController mainController = Controllers.getInstance().mainController;
        mainController.setStatus(Messages.LOADING);
        FileChooser fileChooser = new FileChooser();
        File inputFile = fileChooser.showOpenDialog(Controllers.getInstance().stage);

        if (inputFile == null) {
            mainController.setStatus(Messages.NONE_STATUS);
            return;
        }
        SampleReader sr = new LineSampleReader(inputFile);
        SampleCollection samples = sr.read();
        SamplesParameters.getInstance().setOriginSamples(samples);
        Controllers.getInstance().chartsController.drawChart();
        mainController.setStatus(Messages.NONE_STATUS);

    }

    public void rePlot(ActionEvent actionEvent) {
        MainController mainController = Controllers.getInstance().mainController;
        mainController.setStatus(Messages.REPLOTING);
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        if (sc == null) {
            mainController.showWarn(Messages.NONE_SAMPLE_WARN);
            return;
        }
        Controllers.getInstance().chartsController.drawChart();
        mainController.setStatus(Messages.NONE_STATUS);
    }
}

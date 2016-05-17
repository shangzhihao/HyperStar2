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
        ControllerResource.getInstance().sampleController = this;
        addListener();
    }

    /**
     * add listeners to value changed event
     */
    private void addListener() {
        // sample size changed listener
        sampleSizeSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            int size = newValue.intValue();
            sampleSizeText.setText(String.valueOf(size) + Messages.PERCENT);
            SamplesParameters.getInstance().setSize(size);
        }));
        // sample range changed listener
        sampleRangeFrom.textProperty().addListener((observable, oldValue, newValue) -> {
            double from;
            try {
                from = Double.parseDouble(newValue);
            } catch (Exception e) {
                from = Double.MAX_VALUE;
            }
            SamplesParameters.getInstance().setFrom(from);
        });
        sampleRangeTo.textProperty().addListener((observable, oldValue, newValue) -> {
            double to;
            try {
                to = Double.parseDouble(newValue);
            } catch (Exception e) {
                to = Double.MAX_VALUE;
            }
            SamplesParameters.getInstance().setTo(to);
        });
    }

    /**
     * execute when load sample button is clicked.
     * read samples from selected file
     *
     * @param actionEvent click event
     */
    public void loadSamples(ActionEvent actionEvent) {
        ControllerResource.getInstance().mainController.setStatus(Messages.LOADING);
        FileChooser fileChooser = new FileChooser();
        File inputFile = fileChooser.showOpenDialog(ControllerResource.getInstance().stage);
        if (inputFile == null) {
            ControllerResource.getInstance().mainController.setStatus(Messages.NONE_STATUS);
            return;
        }
        SampleReader sr = new LineSampleReader(inputFile);
        SampleCollection samples = sr.read();
        SamplesParameters.getInstance().setOriginSamples(samples);
        ControllerResource.getInstance().mainController.setStatus(Messages.NONE_STATUS);
        ControllerResource.getInstance().chartsController.drawChart();
    }

    public void rePlot(ActionEvent actionEvent) {
        ControllerResource.getInstance().mainController.setStatus(Messages.REPLOTING);
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        if (sc == null) {
            ControllerResource.getInstance().mainController.showWarn(Messages.NONE_SAMPLE_WARN);
            return;
        }
        ControllerResource.getInstance().chartsController.drawChart();
        ControllerResource.getInstance().mainController.setStatus(Messages.NONE_STATUS);
    }

    public void setInputDisable(boolean isDisable) {
        loadSamplesBtn.setDisable(isDisable);
        rePlotBtn.setDisable(isDisable);
        sampleRangeFrom.setDisable(isDisable);
        sampleRangeTo.setDisable(isDisable);
        sampleSizeSlider.setDisable(isDisable);
    }
}

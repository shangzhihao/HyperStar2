package fu.mi.fitting.controllers;

import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.io.SampleReader;
import fu.mi.fitting.parameters.Messages;
import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    Slider sampleSizeSlider;
    @FXML
    TextField sampleRangeFrom;
    @FXML
    TextField sampleRangeTo;
    @FXML
    Label sampleSizeText;

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
            try {
                double from = Double.parseDouble(newValue);
                SamplesParameters.getInstance().setFrom(from);
            } catch (Exception e) {
            }
        });
        sampleRangeTo.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double to = Double.parseDouble(newValue);
                SamplesParameters.getInstance().setTo(to);
            } catch (Exception e) {
            }
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
}

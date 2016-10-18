package fu.mi.fitting.controllers;

import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.io.SampleReader;
import fu.mi.fitting.parameters.Messages;
import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.utils.CommonUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.util.Optional;

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
        sampleSizeText.textProperty().bindBidirectional(parameters.getSizeProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number num) {
                return String.valueOf(num.intValue()) + Messages.PERCENT;
            }

            @Override
            public Number fromString(String str) {
                String intValue = str.substring(0, str.length() - 1);
                return CommonUtils.strToInt(intValue, 100);
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
        if (SamplesParameters.getInstance().getLimitedSamples().size() == 0) {
            mainController.showWarn(Messages.ILLEGAL_SAMPLE_FILE);
            return;
        }
        Controllers.getInstance().mainController.setFitRes(Optional.empty());
        Controllers.getInstance().chartsController.drawChart();
        mainController.setStatus(Messages.NONE_STATUS);
    }

    public void rePlot(ActionEvent actionEvent) {
        MainController mainController = Controllers.getInstance().mainController;
        mainController.setStatus(Messages.REPLOTING);
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        if (sc.size() == 0) {
            mainController.showWarn(Messages.NONE_SAMPLE_WARN);
            return;
        }
        Controllers.getInstance().chartsController.drawChart();
        mainController.setStatus(Messages.NONE_STATUS);
    }
}

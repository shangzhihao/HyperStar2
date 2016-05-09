package fu.mi.fitting.controllers;

import fu.mi.fitting.fitters.*;
import fu.mi.fitting.parameters.ChartsParameters;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.jfree.data.function.Function2D;

/**
 * Created by shang on 5/6/2016.
 * this controller reacts to the user input form the
 * right side bar, most actions are controlled here.
 */
public class ParameterController {

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
    Slider sampleSizeSlider;
    @FXML
    TextField sampleRangeFrom;
    @FXML
    TextField sampleRangeTo;
    @FXML
    Label sampleSizeText;

    @FXML
    public void initialize() {
        ControllerResource.getInstance().parameterController = this;
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
        // sample size changed listener
        sampleSizeSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            int size = newValue.intValue();
            sampleSizeText.setText(String.valueOf(size) + "%");
            ChartsParameters.getInstance().setSampleSize(size);
        }));
        // sample range changed listener
        sampleRangeFrom.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double from = Double.parseDouble(newValue);
                ChartsParameters.getInstance().setSampleFrom(from);
            } catch (Exception e) {
            }
        });
        sampleRangeTo.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double to = Double.parseDouble(newValue);
                ChartsParameters.getInstance().setSampleTo(to);
            } catch (Exception e) {
            }
        });
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
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        if (sc == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("Please load samples first.");
            alert.showAndWait();
            return;
        }
        Fitter herFitter = FitterFactory.getFitterByName(FitParameters.getInstance().getFitterName(), sc);
        RealDistribution res = herFitter.fit();
        Function2D pdf = d -> res.density(d);
        double start = StatUtils.min(sc.asDoubleArray());
        double end = StatUtils.max(sc.asDoubleArray());
        ControllerResource.getInstance().chartsController.addPDF(pdf, start, end);
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

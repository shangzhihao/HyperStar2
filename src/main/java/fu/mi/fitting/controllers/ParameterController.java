package fu.mi.fitting.controllers;

import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.fitters.FitterFactory;
import fu.mi.fitting.fitters.HyperErlangFitter;
import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.io.SampleReader;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.commons.math3.stat.StatUtils;
import org.jfree.data.function.Function2D;

import java.io.File;

/**
 * Created by shang on 5/6/2016.
 */
public class ParameterController {
    private final ControllerResource controllerResource = ControllerResource.getInstance();
    private final FitParameters fitParameters = FitParameters.getInstance();

    @FXML
    public void initialize() {
        controllerResource.parameterController = this;
    }

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

    public void fitDistribution(ActionEvent actionEvent) {
        SampleCollection sc = controllerResource.mainController.getSampleCollection();
        if (sc == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setContentText("Please load samples first.");
            alert.showAndWait();
            return;
        }
        HyperErlangFitter herFitter = FitterFactory.getHyperErlangFitter(sc);
        herFitter.branch = fitParameters.getBranch();
        HyperErlang res = (HyperErlang) herFitter.fit();
        Function2D pdf = d -> res.density(d);
        double start = StatUtils.min(sc.asDoubleArray());
        double end = StatUtils.max(sc.asDoubleArray());
        controllerResource.chartsController.addPDF(pdf, start, end);
    }
}

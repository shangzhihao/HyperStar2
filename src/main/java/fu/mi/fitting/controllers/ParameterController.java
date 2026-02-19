package fu.mi.fitting.controllers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import fu.mi.fitting.distributions.MarkovArrivalProcess;
import fu.mi.fitting.distributions.PHDistribution;
import fu.mi.fitting.fitters.Fitter;
import fu.mi.fitting.fitters.FitterFactory;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.parameters.Messages;
import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.apache.commons.math3.stat.StatUtils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by shang on 5/6/2016.
 * this is parameter controller, because it is the controller of parameter.fxml.
 * the parameters pane include two other fxml(they have there own controllers),
 * so this controller's responsibility is not as much as its name.
 */
public class ParameterController {

    @FXML
    Button fitBtn;

    @FXML
    public void initialize() {
        Controllers.getInstance().parameterController = this;
    }

    /**
     * executed when export button is clicked
     *
     * @param actionEvent click event
     */
    public void exportBtnAction(ActionEvent actionEvent) {
        MainController mainController = Controllers.getInstance().mainController;
        Optional<PHDistribution> fitRes = mainController.getFitRes();
        if (fitRes.isPresent()) {
            FileChooser fileChooser = new FileChooser();
            File outputFile = fileChooser.showSaveDialog(Controllers.getInstance().stage);
            if(outputFile == null){
                return;
            }
            try {
                Files.write(fitRes.get().toString(), outputFile, Charsets.UTF_8);
            } catch (IOException e) {
                mainController.showWarn(Messages.WRITE_FILE_ERROR);
            }
        } else {
            mainController.showWarn(Messages.NO_FIT_RESULT);
        }
    }

    /**
     * executed when fit button is clicked
     *
     * @param actionEvent click event
     */
    public void fitBtnAction(ActionEvent actionEvent) {
        MainController mainController = Controllers.getInstance().mainController;
        mainController.setFitRes(Optional.empty());
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        if (sc == null || sc.size() == 0) {
            mainController.showWarn(Messages.NONE_SAMPLE_WARN);
            return;
        }
        mainController.setInputDisable(true);
        Thread fitThread = new Thread(() -> {
            try {
                final PHDistribution res = fitDistribution();
                Platform.runLater(() -> {
                    drawResult(res);
                    mainController.setFitRes(Optional.of(res));
                    mainController.setInputDisable(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    mainController.showWarn(e.getMessage() == null ? "fit failed" : e.getMessage());
                    mainController.setInputDisable(false);
                });
            }
        });
        fitThread.setDaemon(true);
        fitThread.start();
    }

    private void drawResult(PHDistribution res) {
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        double start = StatUtils.min(sc.asDoubleArray());
        double end = StatUtils.max(sc.asDoubleArray());
        ChartsController chartsController = Controllers.getInstance().chartsController;
        chartsController.addPDF(res::density, start, end);
        chartsController.addCDF(res::cumulativeProbability, start, end);
        if (chartsController.chartsPane.getTabs().contains(chartsController.corTab)) {
            chartsController.chartsPane.getTabs().remove(chartsController.corTab);
        }
        if (res instanceof MarkovArrivalProcess) {
            chartsController.drawCorrelation();
            chartsController.addCorrelation((MarkovArrivalProcess) res);
            chartsController.chartsPane.getTabs().add(chartsController.corTab);
        }
    }

    /**
     * fit distribution with selected fitter and parameters
     */
    private PHDistribution fitDistribution() {
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        Fitter selectedFitter = FitterFactory.getFitterByName(FitParameters.getInstance().getFitterName(), sc);
        return selectedFitter.fit();

    }
}

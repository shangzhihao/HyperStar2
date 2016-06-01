package fu.mi.fitting.controllers;

import fu.mi.fitting.fitters.Fitter;
import fu.mi.fitting.fitters.FitterFactory;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.parameters.Messages;
import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.stat.StatUtils;

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
     * executed when fit button is clicked
     *
     * @param actionEvent click event
     */
    public void fitBtnAction(ActionEvent actionEvent) {
        // TODO check original samples
        checkSamples();
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        if (sc == null || sc.data.size() == 0) {
            Controllers.getInstance().mainController.showWarn(Messages.NONE_SAMPLE_WARN);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                Controllers.getInstance().mainController.setInputDisable(true);
                fitDistribution();
                Controllers.getInstance().mainController.setInputDisable(false);
            }
        }.start();
    }

    /**
     * fit distribution with selected fitter and parameters
     */
    private void fitDistribution() {
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        Fitter selectedFitter = FitterFactory.getFitterByName(FitParameters.getInstance().getFitterName(), sc);
        RealDistribution res = selectedFitter.fit();
        double start = StatUtils.min(sc.asDoubleArray());
        double end = StatUtils.max(sc.asDoubleArray());
        Controllers.getInstance().chartsController.addPDF(res::density, start, end);
        Controllers.getInstance().chartsController.addCDF(res::cumulativeProbability, start, end);
    }
}

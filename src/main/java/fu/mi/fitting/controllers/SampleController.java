package fu.mi.fitting.controllers;

import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.io.SampleReader;
import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by shang on 5/9/2016.
 */
public class SampleController {

    /**
     * execute when load sample button is clicked.
     * read samples from selected file
     *
     * @param actionEvent click event
     */
    public void loadSamples(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File inputFile = fileChooser.showOpenDialog(ControllerResource.getInstance().stage);
        if (inputFile == null) {
            return;
        }
        SampleReader sr = new LineSampleReader(inputFile);
        SampleCollection samples = sr.read();
        SamplesParameters.getInstance().setOriginSamples(samples);
        ControllerResource.getInstance().chartsController.drawChart();
    }
}

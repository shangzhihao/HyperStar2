package fu.mi.fitting.controllers;

import fu.mi.fitting.sample.SampleCollection;
import javafx.fxml.FXML;

/**
 * Created by shang on 5/1/2016.
 */
public class MainController {

    private SampleCollection sampleCollection;

    @FXML
    public void initialize() {
        ControllerResource.getInstance().mainController = this;
    }

    public SampleCollection getSampleCollection() {
        return sampleCollection;
    }

    public void setSampleCollection(SampleCollection sampleCollection) {
        this.sampleCollection = sampleCollection;
    }
}

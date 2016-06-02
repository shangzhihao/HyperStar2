package fu.mi.fitting.parameters;

import fu.mi.fitting.sample.SampleCollection;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import static fu.mi.fitting.utils.CommonUtils.strToDouble;

/**
 * Created by shang on 5/9/2016.
 * store samples and related parameters
 */
public class SamplesParameters {
    private static final SamplesParameters INSTANCE = new SamplesParameters();
    // how many percents samples used in fitting
    private static final double defaultSize = 100d;
    private SampleCollection originSamples;
    private DoubleProperty size = new SimpleDoubleProperty(defaultSize);
    // sample range, samples in this range will be used in fitting
    // others samples will be ignored
    private StringProperty from = new SimpleStringProperty("min");
    private StringProperty to = new SimpleStringProperty("max");

    private SamplesParameters() {
    }

    public static SamplesParameters getInstance() {
        return INSTANCE;
    }

    public DoubleProperty getSizeProperty() {
        return size;
    }

    public StringProperty getFromProperty() {
        return from;
    }

    public StringProperty getToProperty() {
        return to;
    }

    public void setOriginSamples(SampleCollection originSamples) {
        this.originSamples = originSamples;
    }

    public SampleCollection getLimitedSamples() {
        if (originSamples == null || originSamples.size() == 0) {
            return null;
        }
        double percent = size.doubleValue();
        double min = strToDouble(from.getValue(), Double.MIN_VALUE);
        double max = strToDouble(to.getValue(), Double.MAX_VALUE);
        return originSamples.subSampleCollection(percent, min, max);
    }
}

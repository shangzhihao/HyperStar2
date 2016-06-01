package fu.mi.fitting.parameters;

import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.sample.SampleItem;
import fu.mi.fitting.utils.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

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
        List<SampleItem> res = newArrayList();
        int limit = (int) (size.doubleValue() * originSamples.data.size() / 100);
        double minValue = Utils.strToDouble(from.get(), Double.MIN_VALUE);
        double maxValue = Utils.strToDouble(to.get(), Double.MAX_VALUE);
        SampleItem sample;
        for (int i = 0; i < limit; i++) {
            sample = originSamples.data.get(i);
            if (sample.value <= maxValue && sample.value >= minValue) {
                res.add(sample);
            }
        }
        if (res.size() == 0) {
            res = originSamples.data.stream().collect(toList());
        }
        return new SampleCollection(res);
    }
}

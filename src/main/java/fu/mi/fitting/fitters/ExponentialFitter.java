package fu.mi.fitting.fitters;

import fu.mi.fitting.sample.SampleCollection;
import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 * Created by shang on 5/7/2016.
 */
public class ExponentialFitter extends Fitter<ExponentialDistribution> {
    public static final String FITTER_NAME = "ExponentialFitter";
    public static final String DISPLAY_NAME = "Exponential";

    ExponentialFitter(SampleCollection sc) {
        super(sc);
    }

    @Override
    public ExponentialDistribution fit() {
        return new ExponentialDistribution(samples.getMean());
    }

    @Override
    public String getName() {
        return null;
    }
}

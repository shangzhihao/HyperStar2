package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.Exponential;
import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 5/7/2016.
 * fit an exponential distribution
 */
public class ExponentialFitter extends Fitter<Exponential> {
    public static final String FITTER_NAME = "Exponential";

    ExponentialFitter(SampleCollection sc) {
        super(sc);
    }

    @Override
    public Exponential fit() {
        return new Exponential(samples.getMean());
    }

    @Override
    public String getName() {
        return null;
    }
}

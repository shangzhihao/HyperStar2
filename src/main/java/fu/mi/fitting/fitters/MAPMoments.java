package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 5/23/2016.
 * Fit Markov Arrival Process using joint-moments
 */
public class MAPMoments extends Fitter<HyperErlang> {
    public static final String FITTER_NAME = "Markov Arrival Process";

    MAPMoments(SampleCollection sc) {
        super(sc);
    }

    @Override
    public HyperErlang fit() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}

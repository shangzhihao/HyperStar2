package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.MarkovArrivalProcess;
import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 5/23/2016.
 * Fit Markov Arrival Process using joint-moments
 */
public class MapFitter extends Fitter<MarkovArrivalProcess> {
    public static final String FITTER_NAME = "Markov Arrival Process";

    MapFitter(SampleCollection sc) {
        super(sc);
    }

    @Override
    public MarkovArrivalProcess fit() {
        return null;
    }

    @Override
    public double logLikelihood() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }
}

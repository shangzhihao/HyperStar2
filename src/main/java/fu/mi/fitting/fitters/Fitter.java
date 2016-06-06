package fu.mi.fitting.fitters;


import fu.mi.fitting.distributions.PHDistribution;
import fu.mi.fitting.sample.SampleCollection;
import org.apache.commons.math3.util.FastMath;

import java.util.Optional;

/**
 * Created by shangzhihao on 3/10/16.
 * Abstract Fitter class,
 * all fitters extend this.
 */
public abstract class Fitter<T extends PHDistribution> {
    public SampleCollection samples;
    protected double llh = -1;

    Fitter(SampleCollection sc) {
        this.samples = sc;
    }

    public abstract T fit();

    // TODO: test it
    public double logLikelihood() {
        if (llh == -1) {
            PHDistribution dist = fit();
            Optional<Double> res = samples.getValues().stream().
                    map(value -> FastMath.log(dist.density(value))).
                    reduce((a1, a2) -> a1 + a2);
            llh = res.get();

        }
        return llh;
    }

    public abstract String getName();
}

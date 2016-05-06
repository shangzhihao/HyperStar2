package fu.mi.fitting.fitters;


import fu.mi.fitting.sample.SampleCollection;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * Created by shangzhihao on 3/10/16.
 * Abstract Fitter class,
 * all fitters extend this.
 */
public abstract class Fitter<T extends RealDistribution> {
    public SampleCollection samples;

    Fitter(SampleCollection sc) {
        this.samples = sc;
    }

    public abstract T fit();

    public abstract String getName();
}

package fu.mi.fitting.fitters;


import org.apache.commons.math3.distribution.RealDistribution;

/**
 * Created by shangzhihao on 3/10/16.
 */
public interface Fitter<T extends RealDistribution> {
    T fit();
}

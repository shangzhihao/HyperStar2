package fu.mi.fitting.fitters;


import fu.mi.fitting.distributions.Erlang;
import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shangzhihao on 3/10/16.
 * fit erlang distribution
 * peak = (phase-1)*rate
 * rate = mean/var
 * phase = mean*rate
 */
public abstract class ErlangFitter extends Fitter<Erlang> {
    ErlangFitter(SampleCollection sc) {
        super(sc);
    }

    @Override
    public Erlang fit() {
        Erlang floorRes = fitFloor();
        Erlang ceilRes = fitCeil();
        // floor of log likelihood
        double floorllh = 0;
        // ceil of log likelihood
        double ceilllh = 0;
        for (int i = 0, n = samples.size(); i < n; i++) {
            floorllh += floorRes.logDensity(samples.getValue(i));
            ceilllh += ceilRes.logDensity(samples.getValue(i));
        }
        return floorllh > ceilllh ? floorRes : ceilRes;
    }


    protected abstract Erlang fitCeil();

    protected abstract Erlang fitFloor();

}

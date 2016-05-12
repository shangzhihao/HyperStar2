package fu.mi.fitting.fitters;


import fu.mi.fitting.distributions.Erlang;
import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shangzhihao on 3/10/16.
 *
 *
 * If we let samples mean equal expectation
 * and sample variance equal variance,
 * we will get rate=m/v and phase=m^2/v,
 * that is how I fit erlang distribution in this class.
 * but phase can be only an integer,
 * so I compare the log-likelihood function of the distribution
 * when phase equals floor(phase) and ceil(phase),
 * and choose the greater one.
 */

public class MomErlangFitter extends ErlangFitter{

    public static final String FITTER_NAME = "Erlang";

    MomErlangFitter(SampleCollection sc) {
        super(sc);
    }

    /**
     * get floor(phase) distribution
     */
    protected Erlang fitFloor(){
        double mean = samples.getMean();
        double var = samples.getVar();
        if(var == 0){
            var = Double.MIN_VALUE;
        }
        double rate = mean/var;
        long shape = (long)Math.floor(mean*rate);
        return new Erlang(shape, rate);
    }
    /**
     * get ceil(phase) distribution
     */
    protected Erlang fitCeil(){
        double mean = samples.getMean();
        double var = samples.getVar();
        if(var == 0){
            var = Double.MIN_VALUE;
        }
        double rate = mean/var;
        long shape = (long)Math.ceil(mean*rate);
        return new Erlang(shape, rate);
    }

    @Override
    public String getName() {
        return FITTER_NAME;
    }
}

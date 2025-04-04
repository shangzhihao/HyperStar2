package fu.mi.fitting.fitters;


import fu.mi.fitting.distributions.Erlang;
import fu.mi.fitting.parameters.FitParameters;
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
 *
 * peak = (phase-1)*rate
 * rate = mean/var
 * phase = mean*rate
 */

public class MomErlangFitter extends ErlangFitter{
    public static final String FITTER_NAME = "Erlang";
    private static final int MIN_PHASE = 1;

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
        int phase = (int) Math.floor(mean * mean / var);
        phase = correctPhase(phase);
        double rate = phase / mean;
        phase = correctPhase(phase);
        return new Erlang(phase, rate);
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
        int phase = (int) Math.ceil(mean * mean / var);
        phase = correctPhase(phase);
        double rate = phase / mean;
        phase = correctPhase(phase);
        return new Erlang(phase, rate);
    }

    private int correctPhase(int phase) {
        int maxPhase = FitParameters.getInstance().getMaxPhase();
        if (phase < MIN_PHASE) {
            return MIN_PHASE;
        }
        if (phase > maxPhase) {
            return maxPhase;
        }
        return phase;
    }
    @Override
    public String getName() {
        return FITTER_NAME;
    }
}

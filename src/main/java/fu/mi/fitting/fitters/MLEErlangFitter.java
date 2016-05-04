package fu.mi.fitting.fitters;


import fu.mi.fitting.distributions.Erlang;
import fu.mi.fitting.domains.SampleCollection;

/**
 * Created by shangzhihao on 3/10/16.
 *
 * this is based on the maximization likelihood estimation
 * for gamma distribution.
 * see https://en.wikipedia.org/wiki/Gamma_distribution#Maximum_likelihood_estimation
 * First I calculate shape, then rate.
 * But shape can be only an integer,
 * so I compare the log-likelihood function of the distribution
 * when shape equals floor(shape) and ceil(shape),
 * and choose the greater one.
 */
public class MLEErlangFitter extends ErlangFitter{

    public MLEErlangFitter(SampleCollection samples){
        this.samples = samples;
    }

    protected Erlang fitFloor(){
        double mean = samples.getMean();
        long shape = (long)Math.floor(calcShape());
        double rate = shape/mean;
        return new Erlang(shape, rate);
    }
    protected Erlang fitCeil(){
        double mean = samples.getMean();
        long shape = (long)Math.ceil(calcShape());
        double rate = shape/mean;
        return new Erlang(shape, rate);
    }

    /**
     * see https://en.wikipedia.org/wiki/Gamma_distribution#Maximum_likelihood_estimation
     * */
    private double calcShape(){
        double mean = samples.getMean();
        double logOfmean = Math.log(mean);
        double log = 0;
        for(int i=0, n=samples.data.size(); i < n; i++){
            log += Math.log(samples.data.get(i).value);
        }

        double meanOfLog = log/samples.data.size();
        double s = logOfmean - meanOfLog;
        double res = (s-3)*(s-3) + 24*s;
        res = Math.sqrt(res) + 3 -s;
        return res/(12*s);
    }

}

package fu.mi.fitting.fitters;


import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 4/11/2016.
 */
public class Factory {
    public Fitter getFitter(FitterType type, SampleCollection samples){
        switch (type){
            case HYPER_ERLANG: return new HyperErlangFitter(samples);
            case MLEERLANG: return new MLEErlangFitter(samples);
            case MOMERLANG: return new MomErlangFitter(samples);
            default: return null;
        }
    }
}

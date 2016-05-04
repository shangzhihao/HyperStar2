package fu.mi.fitting.fitters;


import fu.mi.fitting.domains.SampleCollection;
import fu.mi.fitting.fitters.HyperErlangFitter;
import static fu.mi.fitting.fitters.FitterType.*;
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

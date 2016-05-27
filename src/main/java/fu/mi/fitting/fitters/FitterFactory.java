package fu.mi.fitting.fitters;

import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 5/6/2016.
 * create fitter from its fitter name
 */
public class FitterFactory {
    public static Fitter getFitterByName(String fitterName, SampleCollection sc) {
        switch (fitterName) {
            case ExponentialFitter.FITTER_NAME:
                return new ExponentialFitter(sc);
            case MomErlangFitter.FITTER_NAME:
                return new MomErlangFitter(sc);
            case HyperErlangFitter.FITTER_NAME:
                return new HyperErlangFitter(sc);
            case MapFitter.FITTER_NAME:
                return new MapFitter(sc);
            default:
                return new HyperErlangFitter(sc);
        }
    }
}

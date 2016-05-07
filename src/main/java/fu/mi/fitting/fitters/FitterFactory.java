package fu.mi.fitting.fitters;

import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 5/6/2016.
 */
public class FitterFactory {
    private static FitParameters fitParameters = FitParameters.getInstance();



    public static HyperErlangFitter getHyperErlangFitter(SampleCollection sc) {
        return new HyperErlangFitter(sc);
    }

    public static Fitter getFitterByName(String fitterName, SampleCollection sc) {
        switch (fitterName) {
            case ExponentialFitter.FITTER_NAME:
                return new ExponentialFitter(sc);
            case MomErlangFitter.FITTER_NAME:
                return new MomErlangFitter(sc);
            case HyperErlangFitter.FITTER_NAME:
                return new HyperErlangFitter(sc);
            default:
                return new HyperErlangFitter(sc);
        }
    }
}

package fu.mi.fitting.fitters;

import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 5/6/2016.
 */
public class FitterFactory {
    private static FitParameters fitParameters = FitParameters.getInstance();

    public static ErlangFitter getErlangFitter(SampleCollection sc) {
        Class erlangFitterClass = fitParameters.getErlangFitterClass();
        if (erlangFitterClass == MLEErlangFitter.class) {
            return new MLEErlangFitter(sc);
        } else if (erlangFitterClass == MomErlangFitter.class) {
            return new MomErlangFitter(sc);
        }
        return new MomErlangFitter(sc);
    }

    public static HyperErlangFitter getHyperErlangFitter(SampleCollection sc) {
        return new HyperErlangFitter(sc);
    }
}

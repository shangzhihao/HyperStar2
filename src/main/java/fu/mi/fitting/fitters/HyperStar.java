package fu.mi.fitting.fitters;

import cluster.fitter.Hyper_Star_Erlang;
import fu.mi.fitting.distributions.Erlang;
import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.distributions.HyperErlangBranch;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import utilities.Config;

/**
 * Created by shang on 5/7/2016.
 * fit hyper-erlang distribution by the method of Hyper-Star
 */
public class HyperStar extends Fitter<HyperErlang> {
    public static final String FITTER_NAME = "HyperErlang";

    HyperStar(SampleCollection sc) {
        super(sc);
    }

    @Override
    public HyperErlang fit() {
        Hyper_Star_Erlang oldFitter = new Hyper_Star_Erlang();
        Config config = oldFitter.getDefaultConfig();
        config.put("branches", FitParameters.getInstance().getBranch());
        Hyper_Star_Erlang hyperStarRes = oldFitter.fromData(samples.asDoubleList(), config);
        double[] alpha = hyperStarRes.alpha;
        int[] length = hyperStarRes.lengthOfBranch;
        double[] lambda = hyperStarRes.lambdaOfBranch;
        HyperErlang res = new HyperErlang();
        for (int i = 0; i < alpha.length; i++) {
            res.branches.add(new HyperErlangBranch(alpha[i], new Erlang(length[i], lambda[i])));
        }
        return res;
    }

    @Override
    public String getName() {
        return FITTER_NAME;
    }
}

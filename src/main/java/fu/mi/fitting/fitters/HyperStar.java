package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 5/7/2016.
 * fit hyper-erlang distribution by the method of Hyper-Star
 */
public class HyperStar extends Fitter<HyperErlang> {
    private static final String FITTER_NAME = "HyperStar";

    HyperStar(SampleCollection sc) {
        super(sc);
    }

    @Override
    public HyperErlang fit() {
        return null;
    }

    @Override
    public String getName() {
        return FITTER_NAME;
    }
}

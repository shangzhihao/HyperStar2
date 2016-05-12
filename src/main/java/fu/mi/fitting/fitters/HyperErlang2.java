package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 5/12/2016.
 */
public class HyperErlang2 extends Fitter<HyperErlang> {
    public static final String FITTER_NAME = "HyperErlang2";

    HyperErlang2(SampleCollection sc) {
        super(sc);
    }

    @Override
    public HyperErlang fit() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
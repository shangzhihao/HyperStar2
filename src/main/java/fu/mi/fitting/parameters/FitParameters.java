package fu.mi.fitting.parameters;

import fu.mi.fitting.fitters.MomErlangFitter;

/**
 * Created by shang on 5/6/2016.
 * store parameters for fitting
 */
public class FitParameters {
    private static final FitParameters INSTANCE = new FitParameters();
    // branch of Hyper-Erlang distribution
    int branch = 5;
    private Class erlangFitterClass = MomErlangFitter.class;

    private FitParameters() {
    }

    public static FitParameters getInstance() {
        return INSTANCE;
    }

    // getters and setters
    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public Class getErlangFitterClass() {
        return erlangFitterClass;
    }

    public void setErlangFitterClass(Class erlangFitterClass) {
        this.erlangFitterClass = erlangFitterClass;
    }
}

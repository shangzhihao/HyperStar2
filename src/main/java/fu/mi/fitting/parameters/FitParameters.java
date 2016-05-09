package fu.mi.fitting.parameters;

import fu.mi.fitting.fitters.HyperErlangFitter;
import fu.mi.fitting.fitters.MomErlangFitter;

/**
 * Created by shang on 5/6/2016.
 * store parameters for fitting
 */
public class FitParameters {
    private static final FitParameters INSTANCE = new FitParameters();
    // branch of Hyper-Erlang distribution
    private int branch = 2;
    private int kMeans = 10;
    private String erlangFitter = MomErlangFitter.FITTER_NAME;
    private String fitterName = HyperErlangFitter.FITTER_NAME;



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

    public String getErlangFitter() {
        return erlangFitter;
    }

    public void setErlangFitter(String erlangFitter) {
        this.erlangFitter = erlangFitter;
    }

    public String getFitterName() {
        return fitterName;
    }

    public void setFitterName(String fitterName) {
        this.fitterName = fitterName;
    }

    public int getkMeans() {
        return kMeans;
    }

    public void setkMeans(int kMeans) {
        this.kMeans = kMeans;
    }


}

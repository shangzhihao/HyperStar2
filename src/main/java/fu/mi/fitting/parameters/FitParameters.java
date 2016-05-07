package fu.mi.fitting.parameters;

import com.google.common.collect.Maps;
import fu.mi.fitting.fitters.ExponentialFitter;
import fu.mi.fitting.fitters.HyperErlangFitter;
import fu.mi.fitting.fitters.MomErlangFitter;

import java.util.Map;

/**
 * Created by shang on 5/6/2016.
 * store parameters for fitting
 */
public class FitParameters {
    private static final FitParameters INSTANCE = new FitParameters();
    // branch of Hyper-Erlang distribution
    private int branch = 5;
    private int kMeans = 10;
    private String erlangFitter = MomErlangFitter.DISPLAY_NAME;
    private String fitterName = HyperErlangFitter.DISPLAY_NAME;
    private Map<String, String> fitterMap = Maps.newHashMap();
    private FitParameters() {
        fitterMap.put(HyperErlangFitter.DISPLAY_NAME, HyperErlangFitter.FITTER_NAME);
        fitterMap.put(MomErlangFitter.DISPLAY_NAME, MomErlangFitter.FITTER_NAME);
        fitterMap.put(ExponentialFitter.DISPLAY_NAME, ExponentialFitter.FITTER_NAME);
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
        return fitterMap.get(fitterName);
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

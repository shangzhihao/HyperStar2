package fu.mi.fitting.parameters;

import com.google.common.collect.Lists;
import fu.mi.fitting.fitters.HyperStar;
import fu.mi.fitting.fitters.MomErlangFitter;

import java.util.List;

/**
 * Created by shang on 5/6/2016.
 * store parameters for fitting
 */
public class FitParameters {
    private static final FitParameters INSTANCE = new FitParameters();
    // default branch, the fitter will calculate branch
    private final int defaultBranch = -1;
    // branch of Hyper-Erlang distribution
    private int branch = 2;
    private int kMeans = 10;
    private String erlangFitter = MomErlangFitter.FITTER_NAME;
    private String fitterName = HyperStar.FITTER_NAME;
    private List<Double> peaks = Lists.newArrayList();

    private FitParameters() {
    }

    public static FitParameters getInstance() {
        return INSTANCE;
    }

    public void setBranchToDefault() {
        setBranch(defaultBranch);
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


    public List<Double> getPeaks() {
        return peaks;
    }

    public void setPeaks(List<Double> peaks) {
        this.peaks = peaks;
    }

    public void addPeak(double position) {
        peaks.add(position);
    }
}

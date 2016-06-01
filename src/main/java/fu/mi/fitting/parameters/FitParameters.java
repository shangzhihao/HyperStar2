package fu.mi.fitting.parameters;

import com.google.common.collect.Lists;
import fu.mi.fitting.fitters.HyperErlangFitter;
import fu.mi.fitting.fitters.MomErlangFitter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

import static fu.mi.fitting.utils.Utils.strToInt;

/**
 * Created by shang on 5/6/2016.
 * store parameters for fitting
 */
public class FitParameters {
    private static final FitParameters INSTANCE = new FitParameters();
    // default branch, the fitter will calculate branch
    // branch of Hyper-Erlang distribution
    private static final int defaultBranch = 2;
    private static final int DEFAULT_REASSIGN = 20;
    private static final int DEFAULT_OPTIMIZE = 10;
    private static final int DEFAULT_SHUFFLE = 2;
    private static final int DEFAULT_K_MEANS = 10;
    private static final int DEFAULT_MAX_PAHSE = 2000;
    private List<Double> peaks = Lists.newArrayList();
    private String erlangFitter = MomErlangFitter.FITTER_NAME;
    private String fitterName = HyperErlangFitter.FITTER_NAME;

    private StringProperty branch = new SimpleStringProperty(String.valueOf(defaultBranch));
    private StringProperty kMeans = new SimpleStringProperty(String.valueOf(defaultBranch));
    private StringProperty reassign = new SimpleStringProperty(String.valueOf(DEFAULT_REASSIGN));
    private StringProperty optimize = new SimpleStringProperty(String.valueOf(DEFAULT_OPTIMIZE));
    private StringProperty shuffle = new SimpleStringProperty(String.valueOf(DEFAULT_SHUFFLE));
    private StringProperty maxPhase = new SimpleStringProperty(String.valueOf(DEFAULT_MAX_PAHSE));
    private FitParameters() {
    }

    public static FitParameters getInstance() {
        return INSTANCE;
    }

    public StringProperty getMaxPhaseProperty() {
        return maxPhase;
    }

    public int getMaxPhase() {
        return strToInt(maxPhase.get(), DEFAULT_MAX_PAHSE);
    }
    public StringProperty getReassignProperty() {
        return reassign;
    }

    public StringProperty getOptimizeProperty() {
        return optimize;
    }

    public StringProperty getShuffleProperty() {
        return shuffle;
    }

    public int getReassign() {
        return strToInt(reassign.getValue(), DEFAULT_REASSIGN);
    }

    public int getOptimize() {
        return strToInt(optimize.getValue(), DEFAULT_OPTIMIZE);
    }

    public int getShuffle() {
        return strToInt(shuffle.getValue(), DEFAULT_SHUFFLE);
    }

    public StringProperty getBranchProperty() {
        return branch;
    }
    public int getBranch() {
        return strToInt(branch.get(), defaultBranch);
    }

    public StringProperty getkMeansProperty() {
        return kMeans;
    }

    public int getKMeans() {
        return strToInt(kMeans.get(), DEFAULT_K_MEANS);
    }

    public void addPeak(double position) {
        peaks.add(position);
    }

    public void delPeak(double position) {
        peaks.remove(position);
    }

    public List<Double> getPeaks() {
        return peaks;
    }

    public String getFitterName() {
        return fitterName;
    }

    public void setFitterName(String fitterName) {
        this.fitterName = fitterName;
    }
}

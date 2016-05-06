package fu.mi.fitting.parameters;

/**
 * Created by shang on 5/6/2016.
 * store parameters for fitting
 */
public class FitParameters {
    private FitParameters() {
    }

    private static final FitParameters INSTANCE = new FitParameters();

    public static FitParameters getInstance() {
        return INSTANCE;
    }

    public static FitParameters getInstance() {
        return INSTANCE;
    }    // branch of Hyper-Erlang distribution

    int branch = 5;

    // getters and setters
    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }
}

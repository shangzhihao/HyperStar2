package fu.mi.fitting.distributions;

/**
 * Created by shang on 5/20/2016.
 */
public class HyperErlangBranch {
    double probability;
    Erlang dist;

    public HyperErlangBranch(double probability, Erlang dist) {
        this.probability = probability;
        this.dist = dist;
    }
}

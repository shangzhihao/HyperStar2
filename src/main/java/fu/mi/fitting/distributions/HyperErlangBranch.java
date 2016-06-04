package fu.mi.fitting.distributions;

import com.google.common.base.Objects;

/**
 * Created by shang on 5/20/2016.
 * A branch of a HyperErlang Distribution.
 */
public class HyperErlangBranch {
    /**
     * probability of this branch
     */
    public final double probability;
    /**
     * Erlang Distribution of this branch
     */
    public final Erlang dist;

    /**
     * @param probability probability of this branch
     * @param dist        Erlang Distribution of this branch
     */
    public HyperErlangBranch(double probability, Erlang dist) {
        this.probability = probability;
        this.dist = dist;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(probability, dist);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final HyperErlangBranch other = (HyperErlangBranch) obj;
        return Objects.equal(this.probability, other.probability)
                && Objects.equal(this.dist, other.dist);
    }
}

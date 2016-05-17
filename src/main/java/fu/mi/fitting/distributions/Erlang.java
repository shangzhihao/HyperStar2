package fu.mi.fitting.distributions;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.math3.distribution.GammaDistribution;

/**
 * erlang distribution with phase parameter and rate parameter
 */
public class Erlang extends GammaDistribution {
    public int phase;
    public double rate;

    public Erlang(int phase, double rate) {
        super(phase, 1 / rate);
        this.phase = phase;
        this.rate = rate;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(phase, rate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Erlang other = (Erlang) obj;
        return Objects.equal(this.phase, other.phase)
                && Objects.equal(this.rate, other.rate);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("phase", phase)
                .add("rate", rate)
                .toString();
    }
}
package fu.mi.fitting.distributions;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * erlang distribution with phase parameter and rate parameter
 * peak = (phase-1)*rate
 * rate = mean/var
 * phase = mean*rate
 */
public class Erlang extends GammaDistribution {
    public final int phase;
    public final double rate;

    public Erlang(int phase, double rate) {
        super(phase, 1 / rate);
        this.phase = phase;
        this.rate = rate;
    }

    public RealMatrix getD0() {
        RealMatrix res = new Array2DRowRealMatrix(phase, phase);
        for (int i = 0; i < phase - 1; i++) {
            res.setEntry(i, i, -rate);
            res.setEntry(i, i + 1, rate);
        }
        res.setEntry(phase - 1, phase - 1, -rate);
        return res;
    }

    public double expection() {
        return phase / rate;
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
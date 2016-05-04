package fu.mi.fitting.distributions;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * erlang distribution with shape parameter and rate parameter
 */
public class Erlang extends GammaDistribution {
    public long shape;
    public double rate;
    Logger logger = LoggerFactory.getLogger(Erlang.class);
    public Erlang(long shape, double rate){
        super(shape, 1/rate);
        this.shape = shape;
        this.rate = rate;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shape, rate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Erlang other = (Erlang) obj;
        return Objects.equal(this.shape, other.shape)
                && Objects.equal(this.rate, other.rate);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("shape", shape)
                .add("rate", rate)
                .toString();
    }
}
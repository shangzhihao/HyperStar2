package fu.mi.fitting.distributions;

import com.google.common.math.DoubleMath;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.util.FastMath;

/**
 * Created by shang on 6/6/2016.
 */
public class Exponential extends AbstractPHDistribution {
    private ExponentialDistribution dist;

    public Exponential(double mean) {
        dist = new ExponentialDistribution(mean);
    }

    @Override
    protected double calcMoment(int k) {
        return DoubleMath.factorial(k)
                * FastMath.pow(dist.getMean(), k);
    }

    @Override
    public double density(double x) {
        return dist.density(x);
    }

    @Override
    public double cumulativeProbability(double x) {
        return dist.cumulativeProbability(x);
    }
}

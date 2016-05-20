package fu.mi.fitting.distributions;

import com.google.common.collect.Lists;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;


/**
 * Created by shangzhihao on 3/10/16.
 * Hyper-Erlang distribution
 * see https://en.wikipedia.org/wiki/Hyper-Erlang_distribution
 */
public class HyperErlang implements RealDistribution {

    /**
     * initial probability to erlang distribution
     */
    public List<HyperErlangBranch> branches = Lists.newArrayList();
    /**
     * P(X = v)
     */

    @Override
    public double probability(double v) {
        return 0;
    }

    /**
     * PDF
     */
    @Override
    public double density(double v) {
        double res = 0;
        for (HyperErlangBranch branch : branches) {
            res += branch.probability * branch.dist.density(v);
        }
        return res;
    }

    /**
     * CDF
     */
    @Override
    public double cumulativeProbability(double v) {
        BigDecimal res = BigDecimal.ZERO;
        for (HyperErlangBranch branch : branches) {
            res = res.add(cdfBranch(branch, v));
        }
        return 1 - res.doubleValue();
    }

    private BigDecimal cdfBranch(HyperErlangBranch branch, double x) {
        BigDecimal temp;
        BigDecimal res = BigDecimal.ZERO;
        for (int i = 0; i < branch.dist.phase; i++) {
            temp = BigDecimal.valueOf(branch.dist.rate * x);
            temp = temp.pow(i);
            temp = temp.multiply(BigDecimal.valueOf(FastMath.exp(-branch.dist.rate * x)));
            temp = temp.divide(BigDecimal.valueOf(CombinatoricsUtils.factorial(i)), MathContext.DECIMAL128);
            res = res.add(temp);
        }
        res = res.multiply(BigDecimal.valueOf(branch.probability));
        return  res;
    }

    /**
     * P(v < X <= v1)
     */
    @Override
    public double cumulativeProbability(double v, double v1) throws NumberIsTooLargeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double inverseCumulativeProbability(double v) throws OutOfRangeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getNumericalMean() {
        return 0;
    }

    @Override
    public double getNumericalVariance() {
        return 0;
    }

    @Override
    public double getSupportLowerBound() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getSupportUpperBound() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSupportLowerBoundInclusive() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSupportUpperBoundInclusive() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSupportConnected() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reseedRandomGenerator(long l) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double sample() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double[] sample(int i) {
        throw new UnsupportedOperationException();
    }

}


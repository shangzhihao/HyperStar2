package fu.mi.fitting.distributions;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import fu.mi.fitting.sample.SampleCollection;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.RealMatrix;
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
     * branches in hyper-erlang distribution
     */
    public List<HyperErlangBranch> branches = Lists.newArrayList();

    /**
     * get initial probability
     *
     * @return initial probability
     */
    public List<Double> getAlpha() {
        return null;
    }

    /**
     * get transmit matrix
     *
     * @return transmit matrix
     */
    public RealMatrix getD0() {
        return null;
    }
    /**
     * add a branch to distribution
     * @param branch an erlang branch with initial probability
     */
    public void addBranch(HyperErlangBranch branch) {
        branches.add(branch);
    }

    public void addBranch(double init, Erlang erlang) {
        addBranch(new HyperErlangBranch(init, erlang));
    }

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

    /**
     * get log likelihood of given samples
     *
     * @param values samples
     * @return log likelihood
     */
    public double logLikelihood(List<Double> values) {
        double res = 0;
        for (double value : values) {
            res += density(value);
        }
        return res;
    }

    public double logLikelihood(SampleCollection sc) {
        return logLikelihood(sc.asDoubleList());
    }

    private BigDecimal cdfBranch(HyperErlangBranch branch, double x) {
        BigDecimal temp;
        BigDecimal res = BigDecimal.ZERO;
        for (int i = 0; i < branch.dist.phase; i++) {
            temp = BigDecimal.valueOf(branch.dist.rate * x);
            temp = temp.pow(i);
            temp = temp.multiply(BigDecimal.valueOf(FastMath.exp(-branch.dist.rate * x)));
            temp = temp.divide(BigDecimal.valueOf(DoubleMath.factorial(i)), MathContext.DECIMAL128);
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


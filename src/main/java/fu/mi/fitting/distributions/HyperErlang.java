package fu.mi.fitting.distributions;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


/**
 * Created by shangzhihao on 3/10/16.
 * Hyper-Erlang distribution
 * see https://en.wikipedia.org/wiki/Hyper-Erlang_distribution
 */
public class HyperErlang implements RealDistribution {

    /**
     * initial probability to erlang distribution
     */
    public ListMultimap<Double, Erlang> pro2dist  = ArrayListMultimap.create();
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
        Set<Double> keys = pro2dist.keySet();
        for(Double key : keys){
            List<Erlang> erlangs = pro2dist.get(key);
            for(Erlang eralng : erlangs){
                res += key*eralng.density(v);
            }
        }
        return res;
    }

    /**
     * CDF
     */
    @Override
    public double cumulativeProbability(double v) {
        BigDecimal res = BigDecimal.ZERO;
        Set<Double> keys = pro2dist.keySet();
        for(Double key : keys){
            List<Erlang> erlangs = pro2dist.get(key);
            for(Erlang eralng : erlangs){
                res = res.add(cdfBranch(eralng, v));
            }
        }
        return 1 - res.doubleValue();
    }
    private BigDecimal cdfBranch(Erlang erlang, double x){
        BigDecimal res = BigDecimal.ZERO;
        for(int i=0; i<erlang.shape; i++){
            res = BigDecimal.valueOf(erlang.rate*x);
            res = res.pow(i);
            res = res.multiply(BigDecimal.valueOf(FastMath.exp(-erlang.rate * x)));
            res = res.divide(BigDecimal.valueOf(CombinatoricsUtils.factorial(i))).add(res);
        }
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

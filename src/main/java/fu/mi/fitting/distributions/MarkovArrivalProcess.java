package fu.mi.fitting.distributions;

import com.google.common.base.Objects;
import fu.mi.fitting.utils.MathUtils;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;

/**
 * Created by shang on 5/27/2016.
 * Markov Arrival Process
 */
public class MarkovArrivalProcess implements RealDistribution {

    private RealMatrix D0;
    private RealMatrix D1;
    private HyperErlang embedDist;

    public MarkovArrivalProcess(RealMatrix d0, RealMatrix d1, HyperErlang embedDist) {
        D0 = d0;
        D1 = d1;
        this.embedDist = embedDist;
    }

    public double autoCorrelation(int lag) {
        // invers of d0
        RealMatrix d0v = MathUtils.inverseMatrix(D0);
        // matrix P D_0^{-1}*D_1
        RealMatrix P = d0v.multiply(D1);
        // steady state probability of embedded process
        RealVector pi = MathUtils.limitProbability(P);
        double expectation = -Arrays.stream(d0v.operate(pi).toArray()).sum();
        double lambda = 1 / expectation;
        double[] topVector = d0v.multiply(P.power(lag)).multiply(d0v).operate(pi).toArray();
        double top = Arrays.stream(topVector).sum() * lambda * lambda - 1;
        double[] bottomVector = d0v.power(2).operate(pi).toArray();
        double bottom = Arrays.stream(bottomVector).sum() * lambda * lambda * 2 - 1;
        return top / bottom;
    }

    @Override
    public double probability(double x) {
        return 0;
    }

    @Override
    public double density(double x) {
        return getEmbedDist().density(x);
    }

    @Override
    public double cumulativeProbability(double x) {
        return getEmbedDist().cumulativeProbability(x);
    }

    public RealMatrix getD0() {
        return D0;
    }

    public void setD0(RealMatrix d0) {
        D0 = d0;
    }

    public RealMatrix getD1() {
        return D1;
    }

    public void setD1(RealMatrix d1) {
        D1 = d1;
    }

    public HyperErlang getEmbedDist() {
        return embedDist;
    }

    public void setEmbedDist(HyperErlang embedDist) {
        this.embedDist = embedDist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkovArrivalProcess that = (MarkovArrivalProcess) o;
        return Objects.equal(D0, that.D0) &&
                Objects.equal(D1, that.D1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(D0, D1);
    }

    @Override
    public double cumulativeProbability(double x0, double x1) throws NumberIsTooLargeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getNumericalMean() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getNumericalVariance() {
        throw new UnsupportedOperationException();
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
        return false;
    }

    @Override
    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    @Override
    public boolean isSupportConnected() {

        return false;
    }

    @Override
    public void reseedRandomGenerator(long seed) {

    }

    @Override
    public double sample() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double[] sample(int sampleSize) {
        throw new UnsupportedOperationException();
    }
}

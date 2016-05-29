package fu.mi.fitting.distributions;

import com.google.common.base.Objects;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by shang on 5/27/2016.
 * Markov Arrival Process
 */
public class MarkovArrivalProcess implements RealDistribution {

    private RealMatrix D0;
    private RealMatrix D1;

    public MarkovArrivalProcess(RealMatrix d0, RealMatrix d1) {
        D0 = d0;
        D1 = d1;
    }

    @Override
    public double probability(double x) {
        return 0;
    }

    @Override
    public double density(double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double cumulativeProbability(double x) {
        throw new UnsupportedOperationException();
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

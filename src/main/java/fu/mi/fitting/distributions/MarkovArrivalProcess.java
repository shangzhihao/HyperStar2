package fu.mi.fitting.distributions;

import com.google.common.base.Objects;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.jblas.DoubleMatrix;

/**
 * Created by shang on 5/27/2016.
 * Markov Arrival Process
 */
public class MarkovArrivalProcess implements RealDistribution {

    private DoubleMatrix D0;
    private DoubleMatrix D1;

    public MarkovArrivalProcess(DoubleMatrix d0, DoubleMatrix d1) {
        setD0(d0);
        setD1(d1);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkovArrivalProcess that = (MarkovArrivalProcess) o;
        if (!D0.equals(that.D0)) return false;
        return D1.equals(that.D1);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(D0, D1);
    }

    public DoubleMatrix getD0() {
        return D0;
    }

    public void setD0(DoubleMatrix d0) {
        D0 = d0;
    }

    public DoubleMatrix getD1() {
        return D1;
    }

    public void setD1(DoubleMatrix d1) {
        D1 = d1;
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

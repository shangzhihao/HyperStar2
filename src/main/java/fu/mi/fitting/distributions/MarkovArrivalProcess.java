package fu.mi.fitting.distributions;

import com.google.common.base.Objects;
import fu.mi.fitting.utils.MathUtils;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shang on 5/27/2016.
 * Markov Arrival Process
 */
public class MarkovArrivalProcess implements RealDistribution {

    /**
     * expectation of
     */
    public final double expectation;
    private final int dim;
    private final RealMatrix D0;
    private final RealMatrix D1;
    /**
     * embedded process
     * -D_0^{-1}*D_1
     */
    private final RealMatrix P;
    /**
     * just for computing
     * -D_0^{-1}
     */
    private final RealMatrix d0Inverse;
    /**
     * steady probability of P
     * pi*P=pi
     * sum(pi) = 1
     */
    private final RealVector limitProbabitlity;
    private final RealMatrix ones;
    protected Logger logger = LoggerFactory.getLogger(MarkovArrivalProcess.class);

    public MarkovArrivalProcess(RealMatrix d0, RealMatrix d1) {
        D0 = d0;
        D1 = d1;
        dim = D0.getRowDimension();
        // inverse of -d0
        d0Inverse = MathUtils.inverseMatrix(new Array2DRowRealMatrix(dim, dim).subtract(D0));
        // matrix P = -D_0^{-1}*D_1
        P = d0Inverse.multiply(D1);
        limitProbabitlity = MathUtils.limitProbability(P);
        MathUtils.limitProbability(MathUtils.matrixExp(D0.add(D1)));
        ones = MathUtils.getOnes(dim, 1);
        expectation = MathUtils.vectorToRowMatrix(limitProbabitlity)
                .multiply(d0Inverse).multiply(ones).getEntry(0, 0);
    }

    // TODO test
    public double autoCorrelation(int lag) {

        // steady state probability of embedded process
        double lambda = 1 / expectation;
        double top = MathUtils.vectorToRowMatrix(limitProbabitlity)
                .multiply(d0Inverse).multiply(P.power(lag))
                .multiply(d0Inverse).multiply(ones)
                .getEntry(0, 0) * lambda * lambda - 1;
        double bottom = MathUtils.vectorToRowMatrix(limitProbabitlity)
                .multiply(d0Inverse).multiply(d0Inverse).multiply(ones)
                .getEntry(0, 0) * 2 * lambda * lambda - 1;
        return top / bottom;
    }

    public RealVector getLimitProbabitlity() {
        return limitProbabitlity.copy();
    }

    @Override
    public double probability(double x) {
        return 0;
    }

    @Override
    public double density(double x) {
        RealMatrix minusD0 = new Array2DRowRealMatrix(dim, dim).subtract(D0);
        return MathUtils.vectorToRowMatrix(limitProbabitlity)
                .multiply(MathUtils.matrixExp(D0.scalarMultiply(x)))
                .multiply(minusD0.multiply(ones)).getEntry(0, 0);
    }

    @Override
    public double cumulativeProbability(double x) {
        return 1 - MathUtils.vectorToRowMatrix(limitProbabitlity)
                .multiply(MathUtils.matrixExp(D0.scalarMultiply(x)))
                .multiply(ones).getEntry(0, 0);
    }

    public RealMatrix getD0() {
        return D0.copy();
    }


    public RealMatrix getD1() {
        return D1.copy();
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

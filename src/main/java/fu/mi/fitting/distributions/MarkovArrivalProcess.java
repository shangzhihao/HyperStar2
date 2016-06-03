package fu.mi.fitting.distributions;

import com.google.common.base.Objects;
import fu.mi.fitting.utils.MathUtils;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shang on 5/27/2016.
 * Markov Arrival Process
 */
public class MarkovArrivalProcess implements RealDistribution {

    protected Logger logger = LoggerFactory.getLogger(MarkovArrivalProcess.class);
    private RealMatrix D0;
    private RealMatrix D1;
    private HyperErlang embedDist;

    public MarkovArrivalProcess(RealMatrix d0, RealMatrix d1, HyperErlang embedDist) {
        D0 = d0;
        D1 = d1;
        this.embedDist = embedDist;
    }

    // TODO test
    public double autoCorrelation(int lag) {
        int dim = D0.getRowDimension();
        // inverse of d0
        RealMatrix d0Inverse = MathUtils.inverseMatrix(new Array2DRowRealMatrix(dim, dim).subtract(D0));
        // matrix P D_0^{-1}*D_1
        RealMatrix P = d0Inverse.multiply(D1);
        // steady state probability of embedded process
        RealMatrix pi = new Array2DRowRealMatrix(1, dim);
        pi.setRowVector(0, embedDist.getAlpha());
        double expectation = embedDist.expectation();
        logger.info("expection: {}", expectation);
        double lambda = 1 / expectation;
        RealMatrix ones = MathUtils.getOnes(dim, 1);
        double top = pi.scalarMultiply(lambda * lambda)
                .multiply(d0Inverse).multiply(P.power(lag))
                .multiply(d0Inverse).multiply(ones).getEntry(0, 0) - 1;
        double bottom = pi.scalarMultiply(2 * lambda * lambda)
                .multiply(d0Inverse).multiply(d0Inverse)
                .multiply(ones).getEntry(0, 0) - 1;
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

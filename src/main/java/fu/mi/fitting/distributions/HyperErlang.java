package fu.mi.fitting.distributions;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import fu.mi.fitting.utils.MathUtils;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
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
    private List<HyperErlangBranch> branches = Lists.newArrayList();

    public HyperErlang(List<HyperErlangBranch> branches) {
        this.branches = branches;
    }

    public List<HyperErlangBranch> getBranches() {
        List<HyperErlangBranch> res = Lists.newArrayList();
        res.addAll(branches);
        return res;
    }

    /**
     * get initial probability
     *
     * @return initial probability
     */
    public RealVector getAlpha() {
        RealVector res = new ArrayRealVector(getPhase());
        int position = 0;
        for (HyperErlangBranch branch : branches) {
            res.setEntry(position, branch.probability);
            position += branch.dist.phase;
        }
        return res;
    }
    /**
     * get transmit matrix
     *
     * @return transmit matrix
     */
    public RealMatrix getD0() {
        int demession = getPhase();
        RealMatrix res = new Array2DRowRealMatrix(demession, demession);
        int position = 0;
        for (HyperErlangBranch branch : branches) {
            res.setSubMatrix(branch.dist.getD0().getData(), position, position);
            position += branch.dist.phase;
        }
        return res;
    }

    public int getPhase() {
        int res = 0;
        for (HyperErlangBranch branch : branches) {
            res += branch.dist.phase;
        }
        return res;
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

    public double expectation() {
//        double res = 0;
//        for(HyperErlangBranch branch:getBranches()){
//            res += branch.probability*branch.dist.expection();
//        }
//        return res;

        double res;
        RealMatrix d0 = getD0();
        int dim = d0.getColumnDimension();
        RealMatrix zeros = new Array2DRowRealMatrix(dim, dim);
        RealMatrix M = MathUtils.inverseMatrix(zeros.subtract(d0));
        RealMatrix alphaMatrix = new Array2DRowRealMatrix(1, dim);
        alphaMatrix.setRowVector(0, getAlpha());
        RealMatrix ones = MathUtils.getOnes(dim, 1);
        res = alphaMatrix.multiply(M).multiply(ones).getEntry(0, 0);
        return res;
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


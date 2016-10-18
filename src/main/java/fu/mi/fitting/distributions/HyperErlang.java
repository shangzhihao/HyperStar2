package fu.mi.fitting.distributions;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import fu.mi.fitting.utils.CommonUtils;
import fu.mi.fitting.utils.MathUtils;
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
public class HyperErlang extends AbstractPHDistribution {
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

    /**
     * how many phase in this hyper Erlang distribution
     *
     * @return count of phase
     */
    public int getPhase() {
        int res = 0;
        for (HyperErlangBranch branch : branches) {
            res += branch.dist.phase;
        }
        return res;
    }

    @Override
    public double density(double v) {
        double res = 0;
        for (HyperErlangBranch branch : branches) {
            res += branch.probability * branch.dist.density(v);
        }
        return res;
    }

    @Override
    public double cumulativeProbability(double v) {
        BigDecimal res = BigDecimal.ZERO;
        for (HyperErlangBranch branch : branches) {
            res = res.add(cdfBranch(branch, v));
        }
        return 1 - res.doubleValue();
    }

    @Override
    protected double calcMoment(int k) {
        RealMatrix d0 = getD0();
        int dim = d0.getRowDimension();
        RealMatrix d0Inverse = MathUtils.inverseMatrix(new Array2DRowRealMatrix(dim, dim).subtract(d0));
        return DoubleMath.factorial(k)
                * MathUtils.vectorToRowMatrix(getAlpha())
                .multiply(d0Inverse.power(k))
                .multiply(MathUtils.getOnes(dim, 1))
                .getEntry(0, 0);
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
        return res;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Hyper Erlang distribution:\n")
                .append("alpha=").append(CommonUtils.vectorToString(getAlpha())).append('\n')
                .append("Q=").append(CommonUtils.matrixToString(getD0()));
        return res.toString();
    }
}


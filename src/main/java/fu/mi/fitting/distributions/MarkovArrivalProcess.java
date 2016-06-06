package fu.mi.fitting.distributions;

import com.google.common.base.Objects;
import com.google.common.math.DoubleMath;
import fu.mi.fitting.utils.MathUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Created by shang on 5/27/2016.
 * Markov Arrival Process
 */
public class MarkovArrivalProcess extends AbstractPHDistribution {

    /**
     * expectation of distribution
     */
    public final double expectation;
    /**
     * dimension of matrix D0 and D1;
     */
    private final int dim;
    /**
     * transition matrix of MAP
     */
    private final RealMatrix D0;
    private final RealMatrix D1;
    private final Map<Integer, Optional<Double>> moments;
    /**
     * just for computing
     * -D_0^{-1}
     */
    private final RealMatrix d0Inverse;
    /**
     * embedded process
     * -D_0^{-1}*D_1
     */
    private final RealMatrix P;
    /**
     * steady probability of P
     * pi*P=pi
     * sum(pi) = 1
     */
    private final RealVector limitProbabitlity;
    /**
     * a dim by 1 matrix of 1s
     */
    private final RealMatrix ones;
    protected Logger logger = LoggerFactory.getLogger(MarkovArrivalProcess.class);

    public MarkovArrivalProcess(RealMatrix d0, RealMatrix d1) {
        D0 = d0;
        D1 = d1;
        moments = newHashMap();
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

    public double getMoment(int k) {
        Optional<Double> res = moments.getOrDefault(k, Optional.empty());
        if (res.isPresent()) {
            return res.get();
        }
        double moment = DoubleMath.factorial(k)
                * MathUtils.vectorToRowMatrix(limitProbabitlity)
                .multiply(d0Inverse.power(k))
                .multiply(ones).getEntry(0, 0);
        moments.put(k, Optional.of(moment));
        return moment;
    }

    @Override
    protected double calcMoment(int k) {
        return DoubleMath.factorial(k)
                * MathUtils.vectorToRowMatrix(limitProbabitlity)
                .multiply(d0Inverse.power(k))
                .multiply(ones).getEntry(0, 0);
    }

    // TODO test
    public double autoCorrelation(int lag) {
        return MathUtils.vectorToRowMatrix(limitProbabitlity)
                .multiply(d0Inverse).multiply(P.power(lag))
                .multiply(d0Inverse).multiply(ones).getEntry(0, 0);
    }

    public RealVector getLimitProbabitlity() {
        return limitProbabitlity.copy();
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
}

package fu.mi.fitting.utils;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by shang on 6/2/2016.
 * some useful math functions
 */
public class MathUtils {
    private static final double DELTA = 0.00001;

    private MathUtils() {
    }

    /**
     * get inverse matrix of given matrix
     *
     * @param matrix matrix to be inversed
     * @return inverse matrix
     */
    public static RealMatrix inverseMatrix(RealMatrix matrix) {
        int row = matrix.getRowDimension();
        int col = matrix.getColumnDimension();
        if (row != col) {
            throw new IllegalArgumentException("not a nxn matrix");
        }
        // make unit matrix
        RealMatrix unitMatrix = new Array2DRowRealMatrix(row, col);
        IntStream.range(0, row).forEach(i -> unitMatrix.setEntry(i, i, 1));

        DecompositionSolver solver = new LUDecomposition(matrix).getSolver();
        return solver.solve(unitMatrix);
    }

    /**
     * get steady state probability vector of a markov chain
     * state transition probability matrix
     *
     * @param trans transition probability matrix
     * @return steady state probability vector
     */
    public static RealVector limitProbability(RealMatrix trans) {
        EigenDecomposition eigenSolver = new EigenDecomposition(trans);
        RealMatrix diag = eigenSolver.getD();
        double eigenValue;
        double delta;
        for (int i = 0; i < diag.getRowDimension(); i++) {
            eigenValue = diag.getEntry(i, i);
            delta = FastMath.abs(FastMath.abs(eigenValue) - 1);
            if (delta < DELTA) {
                RealVector res = eigenSolver.getEigenvector(i);
                double scale = Arrays.stream(res.toArray()).sum();
                return res.mapMultiply(1 / scale);
            }
        }
        throw new IllegalArgumentException("not a markov transition matrix exception");
    }

}

package fu.mi.fitting.utils;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by shang on 6/2/2016.
 * some useful math functions
 */
public class MathUtils {
    private static final double DELTA = 0.00001;
    static Logger logger = LoggerFactory.getLogger(MathUtils.class);

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

    public static RealMatrix getOnes(int rowDim, int colDim) {
        RealMatrix ones = new Array2DRowRealMatrix(rowDim, colDim);
        for (int i = 0; i < rowDim; i++) {
            for (int j = 0; j < colDim; j++) {
                ones.setEntry(i, j, 1);
            }
        }
        return ones;
    }


    /**
     * get steady state probability vector of a markov chain
     * state transition probability matrix
     *
     * @param trans transition probability matrix
     * @return steady state probability vector
     */
    public static RealMatrix limitProbability(RealMatrix trans) {
        EigenDecomposition eigenSolver = new EigenDecomposition(trans);
        RealMatrix diag = eigenSolver.getD();
        double eigenValue;
        double delta;
        int dim = trans.getColumnDimension();
        for (int i = 0; i < dim; i++) {
            eigenValue = diag.getEntry(i, i);
            delta = FastMath.abs(FastMath.abs(eigenValue) - 1);
            if (delta < DELTA) {
                RealVector vector = eigenSolver.getEigenvector(i);
                double scale = Arrays.stream(vector.toArray()).sum();
                RealMatrix res = new Array2DRowRealMatrix(1, dim);
                res.setRowVector(0, vector.mapMultiply(1 / scale));
                return res;
            }
        }
        throw new IllegalArgumentException("not a markov transition matrix exception");
    }
}

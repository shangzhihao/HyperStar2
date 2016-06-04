package fu.mi.fitting.utils;

import org.apache.commons.math3.linear.*;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;
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

    public static RealMatrix vectorToRowMatrix(RealVector vector) {
        int dim = vector.getDimension();
        RealMatrix res = new Array2DRowRealMatrix(1, dim);
        res.setRowVector(0, vector);
        return res;
    }

    public static RealMatrix vectorToColMatrix(RealVector vector) {
        int dim = vector.getDimension();
        RealMatrix res = new Array2DRowRealMatrix(dim, 1);
        res.setColumnVector(0, vector);
        return res;
    }

    /**
     * get steady state probability vector of a markov chain
     * state transition probability matrix
     *
     * @param matrix transition probability matrix
     * @return steady state probability vector
     */
    public static RealVector limitProbability(RealMatrix matrix) {
        RealMatrix trans = matrix.transpose();
        int row = trans.getRowDimension();
        int col = trans.getColumnDimension();
        if (row != col) {
            throw new IllegalArgumentException("not a square matrix");
        }
        EigenDecomposition eigenSolver = new EigenDecomposition(trans);
        RealMatrix V = eigenSolver.getV();
        double[] real = eigenSolver.getRealEigenvalues();
        for (int i = 0; i < row; i++) {
            if (Math.abs(real[i] - 1.0) < DELTA) {
                RealVector res = V.getColumnVector(i);
                double scale = Arrays.stream(res.toArray()).sum();
                res = res.mapMultiply(1.0 / scale);
                return res;
            }
        }
        throw new IllegalArgumentException("not a markov transition matrix exception");
    }

    public static RealMatrix matrixExp(RealMatrix matrix) {
        DoubleMatrix doubleMatrix = new DoubleMatrix(matrix.getData());
        DoubleMatrix res = MatrixFunctions.expm(doubleMatrix);
        return new Array2DRowRealMatrix(res.toArray2());
    }

}

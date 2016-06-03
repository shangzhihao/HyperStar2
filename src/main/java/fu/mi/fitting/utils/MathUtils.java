package fu.mi.fitting.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

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

    public static RealMatrix getOnes(int rowDim, int colDim) {
        RealMatrix ones = new Array2DRowRealMatrix(rowDim, colDim);
        for (int i = 0; i < rowDim; i++) {
            for (int j = 0; j < colDim; j++) {
                ones.setEntry(i, j, 1);
            }
        }
        return ones;
    }


}

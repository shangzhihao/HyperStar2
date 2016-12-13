package fu.mi.fitting.utils;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shang on 6/2/2016.
 * some useful math functions
 */
public class MathUtils {
    private static final double DELTA = 1E-4;
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
        RealMatrix unitMatrix = getUnitMatrix(col);

        DecompositionSolver solver = new LUDecomposition(matrix).getSolver();
        return solver.solve(unitMatrix);
    }

    public static RealMatrix getUnitMatrix(int dim) {
        RealMatrix res = new Array2DRowRealMatrix(dim, dim);
        for (int i = 0; i < dim; i++) {
            res.setEntry(i, i, 1);
        }
        return res;
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
        RealMatrix trans = matrix.copy();
        int col = trans.getColumnDimension();
        int row = trans.getRowDimension();
        if (col != row) {
            throw new IllegalArgumentException("not a square matrix.");
        }
        RealMatrix ones = getOnes(row, 1);
        RealMatrix sum = trans.multiply(ones);
        for (int i = 0; i < row; i++) {
            if (Math.abs(sum.getEntry(i, 0) - 1) > DELTA) {
                throw new IllegalArgumentException("not a transition matrix.");
            }
        }
        for (int i = 0; i < 14; i++) {
            trans = trans.power(2);
        }
        return trans.getRowVector(row - 1);
    }

    public static RealMatrix matrixExp(RealMatrix matrix) {
        DoubleMatrix doubleMatrix = new DoubleMatrix(matrix.getData());
        DoubleMatrix res = MatrixFunctions.expm(doubleMatrix);
        return new Array2DRowRealMatrix(res.toArray2());
    }

    public static RealMatrix matrixElemProduct(RealMatrix a, RealMatrix b){
        // TODO use exception
        assert a.getColumnDimension() == b.getColumnDimension();
        assert a.getRowDimension() == b.getRowDimension();
        RealMatrix res = new Array2DRowRealMatrix(a.getRowDimension(), a.getColumnDimension());
        for(int i=0;i<a.getRowDimension();i++){
            for(int j=0;j<a.getColumnDimension();j++){
                res.setEntry(i, j, a.getEntry(i,j)*b.getEntry(i,j));
            }
        }
        return res;
    }

    public static double matrixElemSum(RealMatrix m){
        double res = 0;
        for(int i=0;i<m.getRowDimension();i++){
            for(int j=0;j<m.getColumnDimension();j++){
                res += m.getEntry(i,j);
            }
        }
        return  res;
    }
    public static RealMatrix randomMatrix(int row, int col){
        RealMatrix res = new Array2DRowRealMatrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                res.setEntry(i, j, FastMath.random());
            }
        }
        return res;
    }

}

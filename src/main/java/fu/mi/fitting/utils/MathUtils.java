package fu.mi.fitting.utils;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shang on 6/2/2016.
 * some useful math functions
 */
public class MathUtils {
    private static final double DELTA = 1E-3;
    private static final double THETA_13 = 5.371920351148152;
    private static final double[] PADE_13 = {
            64764752532480000.0, 32382376266240000.0, 7771770303897600.0,
            1187353796428800.0, 129060195264000.0, 10559470521600.0,
            670442572800.0, 33522128640.0, 1323241920.0, 40840800.0,
            960960.0, 16380.0, 182.0, 1.0
    };
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
        int row = matrix.getRowDimension();
        int col = matrix.getColumnDimension();
        if (row != col) {
            throw new IllegalArgumentException("not a square matrix.");
        }

        if (row == 0) {
            return matrix.copy();
        }

        double norm = matrix.getNorm();
        int s = 0;
        if (norm > THETA_13) {
            s = (int) FastMath.ceil(FastMath.log(norm / THETA_13) / FastMath.log(2.0));
        }

        RealMatrix a = matrix.scalarMultiply(1.0 / FastMath.pow(2.0, s));
        RealMatrix identity = getUnitMatrix(row);
        RealMatrix a2 = a.multiply(a);
        RealMatrix a4 = a2.multiply(a2);
        RealMatrix a6 = a4.multiply(a2);

        RealMatrix uInner = a6.multiply(a6.scalarMultiply(PADE_13[13])
                        .add(a4.scalarMultiply(PADE_13[11]))
                        .add(a2.scalarMultiply(PADE_13[9])))
                .add(a6.scalarMultiply(PADE_13[7]))
                .add(a4.scalarMultiply(PADE_13[5]))
                .add(a2.scalarMultiply(PADE_13[3]))
                .add(identity.scalarMultiply(PADE_13[1]));
        RealMatrix u = a.multiply(uInner);

        RealMatrix v = a6.multiply(a6.scalarMultiply(PADE_13[12])
                        .add(a4.scalarMultiply(PADE_13[10]))
                        .add(a2.scalarMultiply(PADE_13[8])))
                .add(a6.scalarMultiply(PADE_13[6]))
                .add(a4.scalarMultiply(PADE_13[4]))
                .add(a2.scalarMultiply(PADE_13[2]))
                .add(identity.scalarMultiply(PADE_13[0]));

        RealMatrix p = v.add(u);
        RealMatrix q = v.subtract(u);
        RealMatrix result = new LUDecomposition(q).getSolver().solve(p);

        for (int i = 0; i < s; i++) {
            result = result.multiply(result);
        }
        return result;
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

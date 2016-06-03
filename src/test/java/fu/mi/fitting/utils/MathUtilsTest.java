package fu.mi.fitting.utils;

import com.google.common.collect.Lists;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 6/2/2016.
 */
public class MathUtilsTest {
    private static final double DELTA = 0.0001;

    @Test
    public void inverseMatrix() throws Exception {
        RealMatrix matrix;
        RealMatrix inverse;
        RealMatrix unitMatrix;
        for (int i = 0; i < 100; i++) {
            matrix = randomMatrix();
            inverse = MathUtils.inverseMatrix(matrix);
            unitMatrix = inverse.multiply(matrix);
            checkUnitMatrix(unitMatrix);
        }
    }

    /**
     * check if a matrix is a unit matrix
     *
     * @param unitMatrix
     */
    private void checkUnitMatrix(RealMatrix unitMatrix) {
        int dimession = unitMatrix.getRowDimension();
        assertEquals(dimession, unitMatrix.getColumnDimension());
        for (int i = 0; i < dimession; i++) {
            for (int j = 0; j < dimession; j++) {
                if (i == j) {
                    assertEquals(1, unitMatrix.getEntry(i, j), DELTA);
                } else {
                    assertEquals(0, unitMatrix.getEntry(i, j), DELTA);
                }
            }
        }
    }

    /**
     * get a random square matrix
     *
     * @return random square matrix
     */
    private RealMatrix randomMatrix() {
        Random random = new Random(System.currentTimeMillis());
        int dimession = random.nextInt(5) + 3;
        RealMatrix res = new Array2DRowRealMatrix(dimession, dimession);
        for (int i = 0; i < dimession; i++) {
            for (int j = 0; j < dimession; j++) {
                res.setEntry(i, j, random.nextFloat());
            }
        }
        return res;
    }

    /**
     * calculate limit probability of a transition matrix
     *
     * @param matrix transition matrix
     * @return limit probability
     */
    private RealVector limit(RealMatrix matrix) {
        Map<Integer, RealMatrix> power = new HashMap<>();
        int dim = matrix.getRowDimension();
        power.put(19, matrix.power(20));
        int i;
        for (i = 20; ; i++) {
            power.put(i, matrix.power(i));
            if (power.get(i).getRowVector(0).equals(power.get(i).getRowVector(1))) {
                break;
            }
        }
        System.out.println(i);
        return power.get(i).getRowVector(dim - 1);
    }

    /**
     * make a random markov transition matrix
     *
     * @return random matrix
     */
    private RealMatrix transMatrix() {
        Random random = new Random(System.currentTimeMillis());
        int dimession = random.nextInt(5) + 3;
        RealMatrix res = new Array2DRowRealMatrix(dimession, dimession);
        List<Double> rowSum = Lists.newArrayList();
        IntStream.range(0, 10).forEach(i -> {
            rowSum.add((double) 0);
        });
        double randNum;
        for (int i = 0; i < dimession; i++) {
            for (int j = 0; j < dimession; j++) {
                randNum = random.nextFloat();
                res.addToEntry(i, j, randNum);
                rowSum.set(i, rowSum.get(i) + randNum);
            }
        }
        for (int i = 0; i < dimession; i++) {
            for (int j = 0; j < dimession; j++) {
                res.multiplyEntry(i, j, 1 / rowSum.get(i));
            }
        }
        return res;
    }

    @Test
    public void coVar() {
        double[] x1;
        double[] x2;
        Covariance covariance = new Covariance();
        double covar;

        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 100; i++) {
            int dim = random.nextInt(200) + 5;
            x1 = new double[dim];
            x2 = new double[dim];
            for (int j = 0; j < dim; j++) {
                x1[j] = random.nextDouble();
                x2[j] = x1[j];
            }
            covar = covariance.covariance(x1, x2);
            assertEquals(StatUtils.variance(x1), covar, DELTA);
            assertEquals(StatUtils.variance(x2), covar, DELTA);
            double standVar = FastMath.sqrt(StatUtils.variance(x1))
                    * FastMath.sqrt(StatUtils.variance(x2));
            double corr = covar / standVar;
            assertEquals(1, corr, DELTA);
        }
    }
}
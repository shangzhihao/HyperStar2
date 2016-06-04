package fu.mi.fitting.utils;

import com.google.common.collect.Lists;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 6/2/2016.
 */
public class MathUtilsTest {
    private static final double DELTA = 1E-5;

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

    @Test
    public void limit() {
        for (int i = 0; i < 100; i++) {
            RealMatrix matrix = transMatrix();
            int dim = matrix.getColumnDimension();
            RealVector limitProbability = MathUtils.limitProbability(matrix);
            RealVector expected = matrix.operate(limitProbability);
            assertEquals(limitProbability.getDimension(), dim);
            for (int j = 0; j < dim; j++) {
                assertEquals(expected.getEntry(j),
                        limitProbability.getEntry(j), DELTA);
            }
            assertEquals(1, Arrays.stream(limitProbability.toArray()).sum(), DELTA);
        }
    }

    /**
     * make a random markov transition matrix
     *
     * @return random matrix
     */
    private RealMatrix transMatrix() {
        double[][] ents = {{0.9, 0.04, 0.02, 0.04},
                {0.07, 0.86, 0.01, 0.06},
                {0.08, 0.07, 0.80, 0.05},
                {0.10, 0.02, 0.03, 0.85}};
        RealMatrix res = new Array2DRowRealMatrix(ents);
        return res;
    }

    /**
     * make a random markov transition matrix
     *
     * @return random matrix
     */
    private RealMatrix transMatrix2() {
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
        Random random = new Random();
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
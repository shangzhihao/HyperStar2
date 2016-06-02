package fu.mi.fitting.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 6/2/2016.
 */
// TODO
public class MathUtilsTest {
    private static final double DELTA = 0.0001;

    @Test
    public void inverseMatrix() throws Exception {

    }

    @Test
    public void limitProbability() throws Exception {
        double[][] entries = {{0.6, 0.4}, {0.4, 0.6}};
        RealMatrix matrix = new Array2DRowRealMatrix(entries);
        RealVector res = MathUtils.limitProbability(matrix);
        for (int i = 0; i < res.getDimension(); i++) {
            assertEquals(0.5, res.getEntry(i), DELTA);
        }
    }

}
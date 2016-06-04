import com.google.common.collect.Lists;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.jblas.DoubleMatrix;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 5/27/2016.
 */
public class SimpleTest {
    @Test
    public void queueOrder() {
        RealMatrix cMatrix = new Array2DRowRealMatrix(2, 2);
        cMatrix.setEntry(0, 0, 1);
        cMatrix.setEntry(0, 1, 2);
        cMatrix.setEntry(1, 0, 3);
        cMatrix.setEntry(1, 1, 4);
        DoubleMatrix jMatrix = new DoubleMatrix(cMatrix.getData());
        assertArrayEquals(cMatrix.getData(), jMatrix.toArray2());
    }

    @Test
    public void stream() {
        List<Integer> ints = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            ints.add(i);
        }
        List<Integer> a = ints.stream().collect(Collectors.toList());
        a.remove(0);
        assertEquals(ints.size(), 5);
        assertEquals(a.size(), 4);
    }


    @Test
    public void generator() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            double[][] ents1 = {{random.nextInt(10), random.nextInt(10)}, {random.nextInt(10), random.nextInt(10)}};
            double[][] ents2 = {{random.nextInt(10), random.nextInt(10)}, {random.nextInt(10), random.nextInt(10)}};
            RealMatrix matrix1 = new Array2DRowRealMatrix(ents1);
            RealMatrix matrix2 = new Array2DRowRealMatrix(ents2);
            assertEquals(matrix1.multiply(matrix2).scalarMultiply(3),
                    matrix1.scalarMultiply(3).multiply(matrix2));
        }
    }
}
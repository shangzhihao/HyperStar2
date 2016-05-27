import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;
import org.junit.Test;

/**
 * Created by shang on 5/27/2016.
 */
public class QueueOrderTest {
    @Test
    public void queueOrder() {
        RealMatrix cMatrix = new Array2DRowRealMatrix(2, 2);
        cMatrix.setEntry(0, 0, 1);
        cMatrix.setEntry(0, 1, 2);
        cMatrix.setEntry(1, 0, 3);
        cMatrix.setEntry(1, 1, 4);
        System.out.println(cMatrix.toString());
        DoubleMatrix dMatrix = new DoubleMatrix(cMatrix.getData());
        System.out.println(dMatrix);
        System.out.println(MatrixFunctions.exp(dMatrix).toString());
    }
}

package fu.mi.fitting.distributions;

import fu.mi.fitting.utils.MathUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 5/30/2016.
 * Testcase for Erlang Distribution
 */
public class ErlangTest {
    private double delta = 0.0001;

    @Test
    public void pdf() {
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            Erlang erlang = new Erlang(random.nextInt(5) + 3, random.nextDouble() + 1);
            assertEquals(erlang.density(i), matrixPDF(erlang, i), 0.001);
        }
    }

    @Test
    public void expection() {
        Random random = new Random();
        for (int k = 0; k < 100; k++) {
            Erlang erlang = new Erlang(random.nextInt(5) + 2, random.nextDouble() + 1);
            int dim = erlang.getD0().getRowDimension();
            RealMatrix pi = new Array2DRowRealMatrix(1, dim);
            pi.setEntry(0, 0, 1);
            Array2DRowRealMatrix zeros = new Array2DRowRealMatrix(dim, dim);
            Array2DRowRealMatrix ones = new Array2DRowRealMatrix(dim, 1);
            for (int i = 0; i < dim; i++) {
                ones.setEntry(i, 0, 1);
            }
            RealMatrix d0invers = MathUtils.inverseMatrix(zeros.subtract(erlang.getD0()));
            RealMatrix actual = pi.multiply(d0invers).multiply(ones);
            assertEquals(1, actual.getColumnDimension(), delta);
            assertEquals(1, actual.getRowDimension(), delta);
            assertEquals(erlang.getMean(), actual.getEntry(0, 0), delta);
        }
    }

    private double matrixPDF(Erlang erlang, double x) {
        // x*D0
        RealMatrix D0 = erlang.getD0().scalarMultiply(x);
        // alhpa
        int dimnsion = D0.getColumnDimension();
        Array2DRowRealMatrix alpha = new Array2DRowRealMatrix(1, dimnsion);
        alpha.setEntry(0, 0, 1);

        // mid = e^(x*D0)
        DoubleMatrix expi = MatrixFunctions.expm(new DoubleMatrix(D0.getData()));
        Array2DRowRealMatrix mid = new Array2DRowRealMatrix(expi.toArray2());

        RealMatrix d1 = new Array2DRowRealMatrix(dimnsion, 1);
        d1.setEntry(dimnsion - 1, 0, erlang.rate);
        return alpha.multiply(mid).multiply(d1).getEntry(0, 0);
    }
}
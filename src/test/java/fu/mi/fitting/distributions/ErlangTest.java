package fu.mi.fitting.distributions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 5/30/2016.
 * Testcase for Erlang Distribution
 */
public class ErlangTest {
    @Test
    public void pdf() {
        Erlang erlang = new Erlang(2, 2);
        for (int i = 0; i < 50; i++) {
            assertEquals(erlang.density(i), matrixPDF(erlang, i), 0.001);
        }
    }

    private double matrixPDF(Erlang erlang, double x) {
        RealMatrix D0 = erlang.getD0().scalarMultiply(x);
        int dimnsion = D0.getColumnDimension();

        RealMatrix alpha = new Array2DRowRealMatrix(1, dimnsion);
        alpha.setEntry(0, 0, 1);

        DoubleMatrix expi = MatrixFunctions.expm(new DoubleMatrix(D0.getData()));
        Array2DRowRealMatrix mid = new Array2DRowRealMatrix(expi.toArray2());

        RealMatrix a = new Array2DRowRealMatrix(dimnsion, 1);
        a.setEntry(dimnsion - 1, 0, erlang.rate);

        return alpha.multiply(mid).multiply(a).getEntry(0, 0);
    }
}
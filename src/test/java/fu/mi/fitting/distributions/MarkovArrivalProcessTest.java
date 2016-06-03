package fu.mi.fitting.distributions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 6/3/2016.
 */
public class MarkovArrivalProcessTest {
    @Test
    public void autoCorrelation() throws Exception {
        double[][] d0Entry = {{-10001, 0}, {0, -101}};
        double[][] d1Entry = {{-10000, 1}, {1, -100}};
        RealMatrix d0 = new Array2DRowRealMatrix(d0Entry);
        RealMatrix d1 = new Array2DRowRealMatrix(d1Entry);
        MarkovArrivalProcess map = new MarkovArrivalProcess(d0, d1);
        assertEquals(0.485, map.autoCorrelation(1), 0.001);
    }
}
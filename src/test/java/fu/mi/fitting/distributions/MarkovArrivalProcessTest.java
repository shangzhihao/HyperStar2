package fu.mi.fitting.distributions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

/**
 * Created by shang on 6/3/2016.
 */
public class MarkovArrivalProcessTest {
    @Test
    public void autoCorrelation() throws Exception {
        double[][] d0Entry = {{-9.892,9.892,0.0,0.0,0.0,0.0},
                {0.0,-9.892,0.0,0.0,0.0,0.0},
                {0.0,0.0,-1,1,0.0,0.0},
                {0.0,0.0,0.0,-1,1,0.0},
                {0.0,0.0,0.0,0.0,-1,1},
                {0.0,0.0,0.0,0.0,0.0,-1}};
        double[][] d1Entry = {{0.0,0.0,0.0,0.0,0.0,0.0},
                {6.92,0.0,2.97,0.0,0.0,0.0},
                {0.0,0.0,0.0,0.0,0.0,0.0},
                {0.0,0.0,0.0,0.0,0.0,0.0},
                {0.0,0.0,0.0,0.0,0.0,0.0},
                {0.283,0.0,0.717,0.0,0.0,0.0}};

        RealMatrix d0 = new Array2DRowRealMatrix(d0Entry);
        RealMatrix d1 = new Array2DRowRealMatrix(d1Entry);
        MarkovArrivalProcess map = new MarkovArrivalProcess(d0, d1);
        for (int i = 1; i <= 10; i++) {
            System.out.println(map.autoCorrelation2(i));
        }
    }
}
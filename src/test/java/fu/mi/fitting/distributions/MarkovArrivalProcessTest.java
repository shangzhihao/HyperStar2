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
        double[][] d0Entry = {{-10001, 0}, {0, -101}};
        double[][] d1Entry = {{-10000, 1}, {1, -100}};
//        double[][] d0Entry = {{-5, 5, 0},
//                {1, -2.5, 1},
//                {0, 0, -1}};
//        double[][] d1Entry = {{0, 0, 0},
//                {0.5, 0, 0},
//                {1, 0, 0}};
//        double[][] d0Entry = {
//                {-1.1728, 0, 0},
//                {0, -3.6435, 0},
//                {0, 0, -0.03132}};
//        double[][] d1Entry = {
//                {1.1311, 0.041664, 0},
//                {0.14273, 3.4511, 0.049643},
//                {0.010006, 0, 0.021314}};
        RealMatrix d0 = new Array2DRowRealMatrix(d0Entry);
        RealMatrix d1 = new Array2DRowRealMatrix(d1Entry);
        MarkovArrivalProcess map = new MarkovArrivalProcess(d0, d1);
        for (int i = 1; i <= 20; i++) {
            System.out.println(map.autoCorrelation(i));
        }
    }
}
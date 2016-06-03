package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.MarkovArrivalProcess;
import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.utils.MathUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by shang on 5/31/2016.
 */
public class MapFitterTest {
    private static final double delta = 0.0001;
    static SampleCollection sc;
    protected Logger logger = LoggerFactory.getLogger(MapFitterTest.class);

    @BeforeClass
    public static void setup() {
        FitParameters.getInstance().getBranchProperty().setValue("2");
        sc = new LineSampleReader(new File("E:\\testTraces\\map1")).read();
    }

    @Test
    public void fit() throws Exception {
        MarkovArrivalProcess map = new MapFitter(sc).fit();
        RealMatrix d0 = map.getD0();
        RealMatrix d1 = map.getD1();
        for (int i = 0; i < d0.getRowDimension(); i++) {
            assertEquals(Arrays.stream(d0.add(d1).getRow(i)).sum(), 0, delta);
        }
        int row0 = d0.getRowDimension();
        int col0 = d0.getColumnDimension();
        int row1 = d1.getRowDimension();
        int col1 = d1.getColumnDimension();
        assertEquals(row0, row1);
        assertEquals(col0, col1);
        assertEquals(col0, row0);


        // invers of d0
        RealMatrix d0v = MathUtils.inverseMatrix(d0);

        // matrix P D_0^{-1}*D_1
        RealMatrix P = d0v.multiply(d1);

        // pi
        RealVector pi = map.getEmbedDist().getAlpha();

        // D
        RealMatrix d = d0.add(d1);

        RealVector temp = P.operate(pi);
        for (int i = 0; i < temp.getDimension(); i++) {
            assertTrue(temp.getEntry(i) + pi.getEntry(i) < delta
                    || temp.getEntry(i) - pi.getEntry(i) < delta);
            logger.info("probability: {},{}", i, pi.getEntry(i));
            assertTrue(pi.getEntry(i) >= 0 && pi.getEntry(i) < 1);
        }
        assertEquals(1, Arrays.stream(pi.toArray()).sum(), delta);
        assertArrayEquals(map.getEmbedDist().getAlpha().toArray(), pi.toArray(), delta);
        double sum = Arrays.stream(pi.toArray()).sum();
        assertEquals(1, sum, delta);
        // test autoCorrelation
        double sampleCorr;
        double resCorr;
        for (int i = 0; i < 30; i++) {
            sampleCorr = sc.autocorrelation(i);
            resCorr = map.autoCorrelation(i);
            logger.info("auto correlation: lag {}, expect {}, actual {}", i, sampleCorr, resCorr);
        }
    }

    @Test
    public void autoCorrelation() {

    }
}
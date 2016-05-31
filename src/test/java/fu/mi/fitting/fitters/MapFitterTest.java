package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.MarkovArrivalProcess;
import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 5/31/2016.
 */
public class MapFitterTest {
    static SampleCollection sc;

    @BeforeClass
    public static void setup() {
        FitParameters.getInstance().setBranch(2);
        sc = new LineSampleReader(new File("E:\\testTraces\\map1")).read();
    }

    @Test
    public void fit() throws Exception {
        MarkovArrivalProcess map = new MapFitter(sc).fit();
        RealMatrix d0 = map.getD0();
        RealMatrix d1 = map.getD1();
        for (int i = 0; i < d0.getRowDimension(); i++) {
            assertEquals(Arrays.stream(d0.add(d1).getRow(i)).sum(), 0, 0.001);
        }
    }

    @Test
    public void logLikelihood() throws Exception {

    }

}
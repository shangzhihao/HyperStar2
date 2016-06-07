package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.MarkovArrivalProcess;
import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.utils.MathUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 5/31/2016.
 */
public class MapFitterTest {
    private static final double delta = 0.0001;
    static SampleCollection sc;
    protected Logger logger = LoggerFactory.getLogger(MapFitterTest.class);

    @BeforeClass
    public static void setup() {
        FitParameters.getInstance().getBranchProperty().setValue("4");
        FitParameters.getInstance().getReassignProperty().set("25");
        sc = new LineSampleReader(new File("E:\\testTraces\\lbl3_50k")).read();
    }

    @Test
    public void fit() throws Exception {
        MarkovArrivalProcess map = new MapFitter(sc).fit();
        RealMatrix d0 = map.getD0();
        RealMatrix d1 = map.getD1();
        d0.add(d1).multiply(MathUtils.getOnes(d0.getRowDimension(), 1));
        for (int i = 0; i < d0.getRowDimension(); i++) {
            assertEquals(Arrays.stream(d0.add(d1).getRow(i)).sum(), 0, delta);
        }
        logger.info("samples mean: {}, fitting mean: {}",
                sc.getMean(), map.getMean());
        logger.info("samples var: {}, fitting var: {}",
                sc.getVar(), map.getVariance());
        for (int i = 1; i <= 10; i++) {
            logger.info("{}, {}", sc.autocorrelation(i), map.autoCorrelation(i));
        }
        for (int i = 1; i < 5; i++) {
            logger.info("moment {}, samples: {}, fitting: {}",
                    i, sc.getMoment(i), map.getMoment(i));
        }
    }
}
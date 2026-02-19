package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.Erlang;
import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.distributions.HyperErlangBranch;
import fu.mi.fitting.distributions.MarkovArrivalProcess;
import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

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
        FitParameters.getInstance().getReassignProperty().set("30");
        sc = new LineSampleReader(new File("samples/her.txt")).read();
    }

    @Test
    public void fit() throws Exception {
        List<HyperErlangBranch> branches = newArrayList();
        branches.add(new HyperErlangBranch(0.5714, new Erlang(8, 1.0 / 5)));
        branches.add(new HyperErlangBranch(0.4286, new Erlang(8, 1.0 / 30)));
        RealMatrix d0 = new HyperErlang(branches).getD0();
        int dim = d0.getColumnDimension();
        RealMatrix d1 = new Array2DRowRealMatrix(dim, dim);

        d1.setEntry(7, 0, 0.4 / 5);
        d1.setEntry(7, 8, 0.6 / 5);
        d1.setEntry(15, 0, 0.8 / 30);
        d1.setEntry(15, 8, 0.2 / 30);

        MarkovArrivalProcess map2 = new MarkovArrivalProcess(d0, d1);

        MarkovArrivalProcess map = new MapFitter(sc).fit();
        for (int i = 1; i <= 100; i++) {
            logger.info("{}, {}, {}", sc.autocorrelation(i), map.autoCorrelation(i), map2.autoCorrelation(i));
        }
    }
}

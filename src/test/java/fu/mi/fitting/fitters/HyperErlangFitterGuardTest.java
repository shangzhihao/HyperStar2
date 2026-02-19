package fu.mi.fitting.fitters;

import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.sample.SampleItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HyperErlangFitterGuardTest {

    @Test
    public void fitClampsBranchToSampleSize() {
        FitParameters.getInstance().getBranchProperty().set("99");
        SampleCollection sc = collection(5);
        HyperErlang dist = new HyperErlangFitter(sc).fit();
        assertNotNull(dist);
        assertTrue(dist.getBranches().size() <= sc.size() - 1);
    }

    @Test
    public void fitSingleSampleReturnsSingleBranch() {
        FitParameters.getInstance().getBranchProperty().set("99");
        HyperErlang dist = new HyperErlangFitter(collection(1)).fit();
        assertNotNull(dist);
        assertEquals(1, dist.getBranches().size());
    }

    private SampleCollection collection(int size) {
        List<SampleItem> items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            items.add(new SampleItem(i, i + 1.0));
        }
        return new SampleCollection(items);
    }
}

package fu.mi.fitting.fitters;

import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.sample.SampleItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExponentialFitterTest {

    @Test
    public void getNameReturnsFitterName() {
        ExponentialFitter fitter = new ExponentialFitter(sampleCollection());
        assertEquals(ExponentialFitter.FITTER_NAME, fitter.getName());
    }

    private SampleCollection sampleCollection() {
        List<SampleItem> items = new ArrayList<>();
        items.add(new SampleItem(0, 1.0));
        items.add(new SampleItem(1, 2.0));
        return new SampleCollection(items);
    }
}

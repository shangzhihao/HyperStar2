package fu.mi.fitting.sample;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SampleCollectionAutocorrelationTest {

    @Test(expected = IllegalArgumentException.class)
    public void autocorrelationRejectsZeroLag() {
        sampleCollection().autocorrelation(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void autocorrelationRejectsLagAtSampleSize() {
        SampleCollection sc = sampleCollection();
        sc.autocorrelation(sc.size());
    }

    @Test
    public void autocorrelationAcceptsValidLag() {
        double corr = sampleCollection().autocorrelation(1);
        assertTrue(Double.isFinite(corr));
    }

    private SampleCollection sampleCollection() {
        List<SampleItem> items = new ArrayList<>();
        items.add(new SampleItem(0, 1.0));
        items.add(new SampleItem(1, 2.0));
        items.add(new SampleItem(2, 3.0));
        items.add(new SampleItem(3, 4.0));
        return new SampleCollection(items);
    }
}

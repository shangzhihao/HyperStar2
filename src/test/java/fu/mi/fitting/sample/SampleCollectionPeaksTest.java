package fu.mi.fitting.sample;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SampleCollectionPeaksTest {

    @Test
    public void getPeaksOnEmptySamplesReturnsEmptyList() {
        SampleCollection samples = new SampleCollection(new ArrayList<>());
        assertTrue(samples.getPeaks().isEmpty());
    }

    @Test
    public void getPeaksOnSmallSamplesDoesNotFail() {
        List<SampleItem> items = new ArrayList<>();
        items.add(new SampleItem(0, 0.1));
        items.add(new SampleItem(1, 0.2));
        items.add(new SampleItem(2, 0.3));
        SampleCollection samples = new SampleCollection(items);
        assertNotNull(samples.getPeaks());
    }
}

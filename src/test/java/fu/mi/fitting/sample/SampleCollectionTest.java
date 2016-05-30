package fu.mi.fitting.sample;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by shang on 5/30/2016.
 */
public class SampleCollectionTest {

    SampleCollection sc;

    @BeforeClass
    public void setup() {
        Random rand = new Random();
        List<SampleItem> samples = newArrayList();
        for (int i = 0; i < 10000; i++) {
            samples.add(new SampleItem(i, rand.nextGaussian()));
        }
        sc = new SampleCollection(samples);
    }

    @Test
    public void asDoubleList() throws Exception {

    }

    @Test
    public void asDoubleArray() throws Exception {

    }

    @Test
    public void asHistogramDataset() throws Exception {

    }

    @Test
    public void getMean() throws Exception {

    }

    @Test
    public void getVar() throws Exception {

    }

    @Test
    public void subSampleCollection() throws Exception {

    }

    @Test
    public void subSampleCollection1() throws Exception {

    }

    @Test
    public void getMoment() throws Exception {

    }

    @Test
    public void getMoments() throws Exception {

    }

    @Test
    public void getPeaks() throws Exception {
        sc.getPeaks();
    }

}
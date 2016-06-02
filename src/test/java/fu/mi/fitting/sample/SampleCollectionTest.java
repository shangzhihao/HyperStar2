package fu.mi.fitting.sample;

import fu.mi.fitting.io.LineSampleReader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

/**
 * Created by shang on 5/30/2016.
 *
 */
public class SampleCollectionTest {
    private static SampleCollection sc;

    @BeforeClass
    public static void setup() {
        sc = new LineSampleReader(new File("E:\\testTraces\\map1")).read();
    }

    @Test
    public void asDoubleList() throws Exception {

    }

    @Test
    public void asDoubleArray() throws Exception {

    }

    @Test
    public void subSampleCollection() throws Exception {

    }

    @Test
    public void getMoments() throws Exception {

    }

    @Test
    public void autocorrelation() throws Exception {

    }

    @Test
    public void getPeaks() throws Exception {
        sc.getPeaks().stream().forEach(System.out::println);
    }

}
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
    public void getPeaks() throws Exception {
        sc.getPeaks().stream().forEach(System.out::println);
    }

}
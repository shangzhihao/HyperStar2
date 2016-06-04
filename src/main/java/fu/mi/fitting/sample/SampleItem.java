package fu.mi.fitting.sample;

import com.google.common.collect.Sets;
import org.apache.commons.math3.ml.clustering.Clusterable;

import java.util.Random;
import java.util.Set;

/**
 * Created by shangzhihao on 3/10/16.
 *
 * this class is used for distribution fitting,
 * object of this class is a sample.
 */
public class SampleItem implements Clusterable, Comparable {
    /**
     * id indicate every sample,
     * we can know which group the sample is in after clustering.
     */
    public final int id;
    /**
     * value of sample
     */
    public final double value;
    private Set<Integer> ids = Sets.newHashSet();

    /**
     * @param id sample id
     * @param value sample value
     */
    public SampleItem(int id, double value){
        this.id = id;
        this.value = value;
    }

    /**
     * return a sample object with random id
     * @param value sample value
     */
    public SampleItem(double value){
        Random rnd = new Random(System.currentTimeMillis());
        int rid = rnd.nextInt();
        while (ids.contains(rid)){
            rid = rnd.nextInt();
        }
        this.id = rid;
        this.value = value;

    }

    /**
     * return sample value for clustering
     * @return sample value
     */
    @Override
    public double[] getPoint() {
        return new double[]{value};
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof SampleItem) {
            return Double.compare(value, ((SampleItem) o).value);
        } else {
            throw new IllegalArgumentException();
        }
    }
}

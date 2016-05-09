package fu.mi.fitting.sample;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import fu.mi.fitting.parameters.ChartsParameters;
import org.apache.commons.math3.stat.StatUtils;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import java.util.List;

/**
 * Created by shangzhihao on 3/10/16.
 * <p>
 * this is a wrapper of List of SampleItem
 * I create this class just for convenience
 * of calculate mean and variance of samples,
 * and convert samples to different type
 */
public class SampleCollection {
    /**
     * samples
     */
    public List<SampleItem> data;
    /**
     * values of samples
     */
    private List<Double> values;

    public SampleCollection(List<SampleItem> data) {
        this.data = data;
        values = asDoubleList();
    }

    /**
     * @return values of samples
     */
    public List<Double> asDoubleList() {
        List<Double> res = Lists.newArrayList();
        for (SampleItem sample : data) {
            res.add(sample.value);
        }
        return res;
    }

    public double[] asDoubleArray() {
        return Doubles.toArray(values);
    }

    /**
     * @param key the series key (null not permitted).
     * @return the HistogramDataset
     */
    public HistogramDataset asHistogramDataset(String key) {
        int bins = ChartsParameters.getInstance().getBins();
        HistogramDataset sampleHistogramDataset = new HistogramDataset();
        sampleHistogramDataset.setType(HistogramType.SCALE_AREA_TO_1);
        sampleHistogramDataset.addSeries(key, asDoubleArray(), bins);
        return sampleHistogramDataset;
    }

    /**
     * @return mean of samples
     */
    public double getMean() {
        return StatUtils.mean(Doubles.toArray(values));
    }

    /**
     * @return variance of samples
     */
    public double getVar() {
        return StatUtils.variance(Doubles.toArray(values));
    }

    /**
     * get a sub-collection which contains a part of samples in this collection
     *
     * @param percent how many percents samples the sub-SampleColelction will contain
     * @return sub-SampleCollection
     */
    public SampleCollection subSampleCollection(int percent) {
        double end = (percent / 100.0) * data.size();
        List<SampleItem> res = Lists.newArrayList();
        for (int i = 0; i < end && i < data.size(); i++) {
            res.add(data.get(i));
        }
        return new SampleCollection(res);
    }

    /**
     * get a sub-collection which contains a part of samples in this collection(samples greater or equal to from and less than to)
     *
     * @param from
     * @param to
     * @return
     */
    public SampleCollection subSampleCollection(double from, double to) {
        List<SampleItem> res = Lists.newArrayList();
        for (SampleItem sample : data) {
            if (sample.value < to && sample.value >= from) {
                res.add(sample);
            }
        }
        return new SampleCollection(res);
    }

}

package fu.mi.fitting.sample;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import fu.mi.fitting.parameters.ChartsParameters;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

/**
 * Created by shangzhihao on 3/10/16.
 * <p>
 * this is a wrapper of List of SampleItem
 * I create this class just for convenience
 * of calculate mean and variance of samples,
 * and convert samples to different type
 */
public class SampleCollection {

    private static final int PEAK_DETECT_BINS = 500;
    /**
     * samples
     */
    private final List<SampleItem> data;
    /**
     * values of samples
     */
    private final List<Double> values;

    private final Map<Integer, Optional<Double>> moments = newHashMap();

    private Logger logger = LoggerFactory.getLogger(SampleCollection.class);

    public SampleCollection(List<SampleItem> data) {
        this.data = data;
        values = asDoubleList();
    }

    /**
     * @return values of samples
     */
    public List<Double> asDoubleList() {
        List<Double> res = newArrayList();
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
        return getMoment(1);
    }

    /**
     * @return variance of samples
     */
    public double getVar() {
        return StatUtils.variance(Doubles.toArray(values));
    }

    /**
     * @param percent how many percents samples the sub-SampleCollection will contain
     * @param from    min value sample of sub-SampleCollection
     * @param to      max value sample of sub-SampleCollection
     * @return sub-SampleCollection
     */
    public SampleCollection subSampleCollection(double percent, double from, double to) {
        List<SampleItem> res = newArrayList();
        SampleItem sample;
        double limit = percent * data.size() / 100;
        for (int i = 0; i < limit; i++) {
            sample = data.get(i);
            if (sample.value <= to && sample.value >= from) {
                res.add(sample);
            }
        }
        if (res.size() == 0) {
            res = data.stream().collect(toList());
        }
        return new SampleCollection(res);
    }


    /**
     * get k-order moment
     *
     * @param k order, k should be integer and greater than 0
     * @return k-order moment
     */
    public double getMoment(int k) {
        Optional<Double> res = moments.getOrDefault(k, Optional.empty());
        if (res.isPresent()) {
            return res.get();
        }
        double moment = data.stream()
                .mapToDouble(sample -> FastMath.pow(sample.value, k))
                .sum() / size();
        moments.put(k, Optional.of(moment));
        return moment;
    }

    /**
     * calculate autocorrelation
     *
     * @param lag lag
     * @return autocorrelation
     */
    // TODO test
    public double autocorrelation(int lag) {
        if (lag < 1 || lag >= data.size()) {
            throw new IllegalArgumentException("lag must be in [1, sampleSize-1]");
        }
        List<SampleItem> x1 = data.subList(0, data.size() - lag);
        List<SampleItem> x2 = data.subList(lag, data.size());
        double mMean = IntStream.range(0, x1.size())
                .mapToDouble(i -> x1.get(i).value * x2.get(i).value)
                .sum() / x1.size();
        double cov = mMean - FastMath.pow(getMean(), 2);
        logger.debug("mMean: {}, cov: {}, mean: {}, acf:{}",
                mMean, cov, FastMath.pow(getMean(), 2), cov / getVariance());
        return cov / getVariance();
    }

    public double getVariance() {
        return getMoment(2) - FastMath.pow(getMean(), 2);
    }

    public List<Double> getPeaks() {
        List<Double> sortedSamples = asDoubleList().stream().sorted().collect(toList());
        if (sortedSamples.isEmpty()) {
            return newArrayList();
        }
        Double min = sortedSamples.get(0);
        Double max = sortedSamples.get(sortedSamples.size() - 1);
        if (max <= min) {
            return newArrayList();
        }
        double range = (max - min) / PEAK_DETECT_BINS;
        if (range <= 0) {
            return newArrayList();
        }
        Map<Double, Integer> hist = newHashMap();
        int bin;
        double x;
        for (Double sample : sortedSamples) {
            bin = (int) FastMath.floor((sample - min) / range);
            if (bin >= PEAK_DETECT_BINS) {
                bin = PEAK_DETECT_BINS - 1;
            }
            x = min + (bin + 0.5) * range;
            hist.put(x, hist.getOrDefault(x, 0) + 1);
        }
        List<WeightedObservedPoint> points = hist.keySet().stream().map(key -> new WeightedObservedPoint
                (1, key, hist.get(key).doubleValue())).collect(toList());
        if (points.size() < 7) {
            return newArrayList();
        }
        double[] curve = PolynomialCurveFitter.create(6).fit(points);
        PolynomialFunction curveFunction = new PolynomialFunction(curve);

        List<Double> values = newArrayList();
        List<Double> res = newArrayList();
        for (double v = min; v < max; v += 0.5) {
            values.add(curveFunction.value(v));
        }
        for (int i = 1; i < values.size() - 1; i++) {
            if (values.get(i) <= 0) {
                continue;
            }
            if (values.get(i) > values.get(i + 1) && values.get(i) > values.get(i - 1)) {
                res.add(values.get(i));
            }
        }
        return res;
    }

    public List<Double> getValues() {
        List<Double> res = Lists.newArrayList();
        res.addAll(values);
        return res;
    }

    public List<SampleItem> getData() {
        List<SampleItem> res = Lists.newArrayList();
        res.addAll(data);
        return res;
    }

    public int size() {
        return data.size();
    }

    public double getValue(int i) {
        return data.get(i).value;
    }

    public SampleItem getSample(int i) {
        return data.get(i);
    }
}

package fu.mi.fitting.sample;

import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import fu.mi.fitting.parameters.ChartsParameters;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.FastMath;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
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

    private static final int PEAK_DETECT_BINS = 200;

    /**
     * samples
     */
    private List<SampleItem> data;
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
        return StatUtils.mean(Doubles.toArray(values));
    }

    /**
     * @return variance of samples
     */
    public double getVar() {
        return StatUtils.variance(Doubles.toArray(values));
    }

    /**
     *
     * @param percent how many percents samples the sub-SampleCollection will contain
     * @param from min value sample of sub-SampleCollection
     * @param to max value sample of sub-SampleCollection
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
        List<Double> samplesPower = newArrayList();
        for (SampleItem sample : data) {
            samplesPower.add(FastMath.pow(sample.value, k));
        }
        double res = StatUtils.mean(Doubles.toArray(samplesPower));
        return res;
    }

    /**
     * get n-order moments form begin(include) to end(include)
     *
     * @param begin the min order
     * @param end   the max order
     * @return moments form begin to end
     */
    public List<Double> getMoments(int begin, int end) {
        List<Double> res = newArrayList();
        for (int i = begin; i <= end; i++) {
            res.add(getMoment(i));
        }
        return res;
    }

    /**
     * calculate autocorrelation
     *
     * @param lag lag
     * @return autocorrelation
     */
    // TODO test
    public double autocorrelation(int lag) {
        List<SampleItem> x1 = data.subList(0, data.size() - lag);
        List<SampleItem> x2 = data.subList(lag, data.size());
        double[] xArray = new SampleCollection(x1).asDoubleArray();
        double[] yArray = new SampleCollection(x2).asDoubleArray();
        Covariance covariance = new Covariance();
        double covar = covariance.covariance(xArray, yArray);
        double standVar = FastMath.sqrt(StatUtils.variance(xArray))
                * FastMath.sqrt(StatUtils.variance(yArray));
        return covar / standVar;
//        PearsonsCorrelation corCalc = new PearsonsCorrelation();
//        return corCalc.correlation(xArray, yArray);
    }

    public List<Double> getPeaks() {
        List<Double> sortedSamples = asDoubleList().stream().sorted().collect(toList());
        Double min = sortedSamples.get(0);
        Double max = sortedSamples.get(sortedSamples.size() - 1);
        int range = sortedSamples.size() / PEAK_DETECT_BINS;
        Map<Double, Integer> hist = Maps.newHashMap();
        double current;
        double x;
        for (Double samle : sortedSamples) {
            current = min;
            while (current < max) {
                current += range;
                if (samle < current) {
                    x = current - (range / 2);
                    hist.put(x, hist.getOrDefault(x, 0) + 1);
                }
            }
        }
        double weight = 1.0 / PEAK_DETECT_BINS;
        List<WeightedObservedPoint> points = hist.keySet().stream().map(key -> new WeightedObservedPoint
                (weight, key, hist.get(key).doubleValue())).collect(toList());
        double[] curve = PolynomialCurveFitter.create(12).fit(points);
        PolynomialFunction polynomial = new PolynomialFunction(curve);
        PolynomialFunction derivative = polynomial.polynomialDerivative();
        LaguerreSolver solver = new LaguerreSolver();
        Complex[] roots = solver.solveAllComplex(derivative.getCoefficients(), min, 200);
        Arrays.stream(roots).forEach(System.out::println);
        List<Double> res = newArrayList();
        List<Double> realRoots = Arrays.stream(roots).filter(root -> root.getImaginary() == 0)
                .map(Complex::getReal).sorted().collect(toList());
        double tend = derivative.value(realRoots.get(0) - 0.0001);
        if (tend < 0) {
            for (int i = 1; i < realRoots.size(); i += 2) {
                res.add(realRoots.get(i));
            }
        } else {
            for (int i = 0; i < realRoots.size(); i += 2) {
                res.add(realRoots.get(i));
            }
        }
        return res;
    }

    public List<Double> getValues() {
        return values;
    }

    public List<SampleItem> getData() {
        return data;
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

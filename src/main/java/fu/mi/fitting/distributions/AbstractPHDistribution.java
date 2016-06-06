package fu.mi.fitting.distributions;

import org.apache.commons.math3.util.FastMath;

import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Created by shang on 6/6/2016.
 */
public abstract class AbstractPHDistribution implements PHDistribution {
    protected Map<Integer, Optional<Double>> moments = newHashMap();

    @Override
    public double getMoment(int k) {
        Optional<Double> res = moments.getOrDefault(k, Optional.empty());
        if (res.isPresent()) {
            return res.get();
        }
        double moment = calcMoment(k);
        moments.put(k, Optional.of(moment));
        return moment;
    }

    @Override
    public double getMean() {
        return getMoment(1);
    }

    @Override
    public double getVariance() {
        return getMoment(2) - FastMath.pow(getMoment(1), 2);
    }

    protected abstract double calcMoment(int k);
}

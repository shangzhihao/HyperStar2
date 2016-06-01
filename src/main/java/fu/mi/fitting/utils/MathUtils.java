package fu.mi.fitting.utils;

import com.google.common.primitives.Doubles;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;

import java.util.List;

/**
 * Created by shang on 6/1/2016.
 * mathematical utils
 */
public class MathUtils {

    /**
     * calculate correlation of two random variables
     *
     * @param x1 random variable
     * @param x2 random variable
     * @return correlation
     */
    public static double correlation(List<Double> x1, List<Double> x2) {
        if (x1.size() != x2.size()) {
            throw new IllegalArgumentException();
        }
        double mean1 = StatUtils.mean(Doubles.toArray(x1));
        double mean2 = StatUtils.mean(Doubles.toArray(x2));
        double top = 0;
        double left = 0;
        double right = 0;
        for (int i = 0; i < x1.size(); i++) {
            top += (x1.get(i) - mean1) * (x2.get(i) - mean2);
            left += FastMath.pow(x1.get(i) - mean1, 2);
            right += FastMath.pow(x2.get(i) - mean2, 2);
        }
        left = FastMath.sqrt(left);
        right = FastMath.sqrt(right);
        return top / (left * right);
    }
}

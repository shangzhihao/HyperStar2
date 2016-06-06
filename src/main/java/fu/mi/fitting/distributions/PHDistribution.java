package fu.mi.fitting.distributions;

/**
 * Created by shang on 6/6/2016.
 */
public interface PHDistribution {
    /**
     * @param x the point at which the PDF is evaluated
     * @return value of PDF at point x
     */
    double density(double x);

    /**
     * @param x the point at which the CDF is evaluated
     * @return value of PDF at point x
     */
    double cumulativeProbability(double x);

    /**
     * expectation of this distribution
     *
     * @return expectation
     */
    double getMean();

    /**
     * variance of this distribution
     *
     * @return variance
     */
    double getVariance();

    /**
     * the kth moment of distribution
     *
     * @param k moment order
     * @return the kth moment
     */
    double getMoment(int k);
}

package fu.mi.fitting.pso;

import fu.mi.fitting.distributions.MarkovArrivalProcess;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

import java.util.Arrays;

/**
 * Created by shang on 12/13/2016.
 */
public class Particle {

    int lag = 10;
    private final PSO pso;
    RealMatrix count;
    private double fittness;
    RealMatrix d1;

    Particle(PSO pso, RealMatrix count){
        this.pso = pso;
        this.count = count;
        calcFittness();
    }


    void calcFittness(){
        d1 = counterToRate();
        MarkovArrivalProcess map = new MarkovArrivalProcess(pso.d0, d1);
        fittness = 0;
        int maxLag = Math.min(lag, pso.samples.size() - 2);
        if (maxLag < 1) {
            fittness = Double.MAX_VALUE;
            return;
        }
        for (int i = 1; i <= maxLag; i++) {
            fittness += FastMath.pow(pso.samples.autocorrelation(i+1)-
                    map.autoCorrelation(i+1), 2);
        }
    }
    double getFittness(){
        return fittness;
    }

    public void updateCount(RealMatrix gbest, RealMatrix lbest){
        count = count.add(gbest.subtract(count).scalarMultiply(pso.lr)).
                add(lbest.subtract(count).scalarMultiply(pso.lr));
        calcFittness();
    }

    /**
     * transition count to transition rate
     * @return D1 matrix
     */
    private RealMatrix counterToRate() {
        int dim = pso.d0.getRowDimension();
        RealMatrix res = new Array2DRowRealMatrix(dim, dim);
        int begin = -1;
        int end = -1;
        for (int i = 0; i < count.getRowDimension(); i++) {
            for (int j = 0; j < count.getColumnDimension(); j++){
                begin = pso.clusterBegins.get(i);
                end = pso.clusterEnds.get(j);
                res.setEntry(end, begin, count.getEntry(i, j));
            }
        }
        double d0RowSum;
        double d1RowSum;
        double scale;
        for (int i = 0; i < res.getRowDimension(); i++) {
            d1RowSum = Arrays.stream(res.getRow(i)).sum();
            d0RowSum = Arrays.stream(pso.d0.getRow(i)).sum();
            if (d0RowSum == 0 || d1RowSum == 0) {
                continue;
            }
            scale = -d0RowSum / d1RowSum;
            for (int j = 0; j < res.getColumnDimension(); j++) {
                res.multiplyEntry(i, j, scale);
            }
        }
        return res;
    }
}

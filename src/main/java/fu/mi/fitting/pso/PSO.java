package fu.mi.fitting.pso;

import com.google.common.collect.Lists;
import fu.mi.fitting.fitters.MapFitter;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.utils.MathUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by shang on 12/7/2016.
 */
public class PSO {
    int maxGeneration = 50;
    int group = 15;
    int groupSize = 10;
    double lr = 0.1;

    RealMatrix ancestor;
    RealMatrix d0;
    final SampleCollection samples;
    final Map<Integer, Integer> clusterBegins;
    final Map<Integer, Integer> clusterEnds;
    List<Group> groups = Lists.newArrayList();

    Logger logger = LoggerFactory.getLogger(PSO.class);
    public PSO(RealMatrix d0, RealMatrix ancestor, Map<Integer, Integer> clusterBegins,
               Map<Integer, Integer> clusterEnds, SampleCollection samples) {
        this.samples = samples;
        this.ancestor = ancestor;
        this.d0 = d0;
        this.clusterBegins = clusterBegins;
        this.clusterEnds = clusterEnds;
    }

    public RealMatrix optimize(){
        groups = Lists.newArrayList();
        int row = ancestor.getRowDimension();
        int col = ancestor.getColumnDimension();
        for (int i = 0; i < group; i++) {
            Group g = new Group();
            g.particles.add(new Particle(this, ancestor));
            for (int j = 1; j < groupSize; j++) {
                g.particles.add(new Particle(this, ancestor.multiply(MathUtils.randomMatrix(row, col))));
            }
            groups.add(g);
        }
        for (int i = 0; i < maxGeneration; i++) {
            updateGroups(gBest());
            logger.info("the {}th generation, fittness: {}", i, gBest().getFittness());
        }
        return gBest().d1;
    }

    private void updateGroups(Particle gbest) {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).update(gbest);
        }
    }


    private Particle gBest(){
        List<Particle> particles = lBest();
        int res = 0;
        double best = -1;
        double fittness = Double.MAX_VALUE;
        for (int i = 0; i < particles.size(); i++) {
            fittness = particles.get(i).getFittness();
            if (fittness < best) {
                best = fittness;
                res = i;
            }
        }
        return particles.get(res);
    }
    private List<Particle> lBest(){
        List<Particle> res = Lists.newArrayList();
        for (int i = 0; i < groups.size(); i++) {
            res.add(groups.get(i).getBest());
        }
        return res;
    }
}

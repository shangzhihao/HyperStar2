package fu.mi.fitting.pso;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by shang on 12/7/2016.
 */
public class Group {
    List<Particle> particles = newArrayList();

    Particle getBest() {
        int res = 0;
        double best = Double.MAX_VALUE;
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

    public void update(Particle gbest) {
        Particle lbest = getBest();
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).updateCount(gbest.count, lbest.count);
        }
    }
}

package fu.mi.fitting.distributions;

import com.google.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by shang on 6/3/2016.
 */
public class HyperErlangTest {

    private static final double delta = 0.0001;
    private static HyperErlang hErD = new HyperErlang();
    protected Logger logger = LoggerFactory.getLogger(HyperErlangTest.class);

    @BeforeClass
    public static void setup() {
        Random random = new Random();
        List<Double> initial = Lists.newArrayList();
        int branch = random.nextInt(5) + 3;
        double sum = 0;
        double proba;
        for (int i = 0; i < branch; i++) {
            proba = random.nextDouble();
            initial.add(proba);
            sum += proba;
        }
        for (int i = 0; i < branch; i++) {
            hErD.addBranch(initial.get(i) / sum,
                    new Erlang(random.nextInt(4) + 1, random.nextDouble()));
        }
    }

    @Test
    public void getAlpha() throws Exception {

    }

    @Test
    public void getD0() throws Exception {

    }

    @Test
    public void getPhase() throws Exception {

    }

    @Test
    public void expectation() throws Exception {
        double actual = 0;
        for (HyperErlangBranch branch : hErD.getBranches()) {
            actual += branch.probability * branch.dist.expection();
        }
        assertEquals(actual, hErD.expectation(), delta);
    }

}
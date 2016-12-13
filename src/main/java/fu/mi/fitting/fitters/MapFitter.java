package fu.mi.fitting.fitters;

import com.google.common.collect.Maps;
import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.distributions.HyperErlangBranch;
import fu.mi.fitting.distributions.MarkovArrivalProcess;
import fu.mi.fitting.pso.PSO;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.sample.SampleItem;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shang on 5/23/2016.
 * Fit Markov Arrival Process using joint-moments
 */
public class MapFitter extends Fitter<MarkovArrivalProcess> {
    public static final String FITTER_NAME = "MarkovArrivalProcess";
    Logger logger = LoggerFactory.getLogger(MapFitter.class);
    private HyperErlang hErD;
    private int phase;

    MapFitter(SampleCollection sc) {
        super(sc);
    }

    @Override
    public MarkovArrivalProcess fit() {
        HyperErlangFitter fitter = (HyperErlangFitter) FitterFactory.getFitterByName(HyperErlangFitter.FITTER_NAME, samples);
        hErD = fitter.fit();
        phase = hErD.getPhase();
        RealMatrix d0 = hErD.getD0();
        RealMatrix cs = transCountFromCluster(fitter.getCluster());
        Map<Integer, Integer> heads = getClusterHeads();
        Map<Integer, Integer> tails = getClusterTails();
        PSO pso = new PSO(d0, cs, heads, tails, samples);
        RealMatrix d1 = pso.optimize();
        logger.info("d0: {}", d0.toString());
        logger.info("d1: {}", d1.toString());
        return new MarkovArrivalProcess(d0, d1);
    }
    /**
     * get transitions count of clusters
     * @param cluster samples clusters
     * @return transition count matrix
     */
    private RealMatrix transCountFromCluster(List<SampleCollection> cluster) {
        int dim = cluster.size();
        RealMatrix res = new Array2DRowRealMatrix(dim, dim);
        Map<Integer, Integer> id2cluster = Maps.newHashMap();
        for (int i = 0; i < cluster.size(); i++) {
            for (SampleItem sample : cluster.get(i).getData()) {
                id2cluster.put(sample.id, i);
            }
        }
        // collect samples' id
        List<Integer> ids = samples.getData().stream().map(sample -> sample.id)
                .sorted().collect(Collectors.toList());
        // construct transfer count matrix;
        int from = -1;
        int to = -1;
        for (int i = 0; i < ids.size() - 1; i++) {
            from = id2cluster.get(ids.get(i));
            to = id2cluster.get(ids.get(i + 1));
            res.setEntry(from, to, res.getEntry(from, to) + 1);
        }
        return res;
    }
    /**
     * get beginning position of erlang branch
     * @return beginning positions
     */
    private Map<Integer, Integer> getClusterHeads() {
        Map<Integer, Integer> res = Maps.newHashMap();
        List<HyperErlangBranch> branches = hErD.getBranches();
        int begin = 0;
        for (int i = 0; i < branches.size(); i++) {
            res.put(i, begin);
            begin = begin + branches.get(i).dist.phase;
        }
        return res;
    }

    /**
     * get end position of erlang branch
     * @return end positions
     */
    private Map<Integer, Integer> getClusterTails() {
        Map<Integer, Integer> res = Maps.newHashMap();
        int begin = 0;
        List<HyperErlangBranch> branches = hErD.getBranches();
        for (int i = 0; i < branches.size(); i++) {
            begin = begin + branches.get(i).dist.phase;
            res.put(i, begin - 1);
        }
        return res;
    }
    @Override
    public String getName() {
        return FITTER_NAME;
    }
}


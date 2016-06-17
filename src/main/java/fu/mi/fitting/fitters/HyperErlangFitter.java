package fu.mi.fitting.fitters;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import fu.mi.fitting.distributions.Erlang;
import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.distributions.HyperErlangBranch;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.sample.SampleItem;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.DoubleStream;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by shangzhihao on 3/10/16.
 * <p>
 * this is used to fit Hyper Erlang distribution.
 * there are 3 steps for that:
 * 1) cluster samples using k-means algorithm
 * 2) fit each cluster’s samples with an Erlang distribution
 * 3) the assignment of samples to clusters is refined iteratively
 * until either convergence is reached or
 * a maximum number of rounds has elapsed.
 */
public class HyperErlangFitter extends Fitter {

    public static final String FITTER_NAME = "Hyper-Erlang";
    private Logger logger = LoggerFactory.getLogger(HyperErlangFitter.class);
    private HyperErlang fitResult = null;
    private List<SampleCollection> cluster;

    HyperErlangFitter(SampleCollection sc) {
        super(sc);
    }

    @Override
    public HyperErlang fit() {
        if (fitResult == null) {
            HyperErlangFitter bestFitter = refinement(initFit());
            setCluster(bestFitter.getCluster());
            branchFit();
        }
        return fitResult;
    }

    /**
     * refine initial fitting result
     *
     * @param roughRes initial fitting result
     * @return refined result, this would be the final result
     */
    private HyperErlangFitter refinement(HyperErlang roughRes) {
        FitParameters fitParameters = FitParameters.getInstance();

        int reassignment = fitParameters.getReassign();
        int numOfShuffles = fitParameters.getShuffle();

        TreeSet<HyperErlangFitter> results = new TreeSet<>(new HyperErlangComparator());
        TreeSet<HyperErlangFitter> loopRes = new TreeSet<>(new HyperErlangComparator());
        List<HyperErlangFitter> resCandidate;
        results.addAll(shuffle(roughRes, numOfShuffles));
        for (int i = 0; i < reassignment; i++) {
            logger.info("the {}th reassign of {}, llh: {}.", i, reassignment, results.first().logLikelihood());
            loopRes.clear();
            for (HyperErlangFitter fitter : results) {
                resCandidate = shuffle(fitter.fit(), numOfShuffles);
                logger.debug("log likelihood: {}", fitter.logLikelihood());
                loopRes.addAll(resCandidate);
            }
            results.addAll(loopRes);
            cleanRseult(results);
        }
        return results.first();
    }

    private void cleanRseult(TreeSet<HyperErlangFitter> results) {
        TreeSet<HyperErlangFitter> bestRes = new TreeSet<>(new HyperErlangComparator());
        int maxCandidate = FitParameters.getInstance().getOptimize();
        for (int i = 0; i < maxCandidate && i < results.size(); i++) {
            if (results.first() != null) {
                bestRes.add(results.pollFirst());
            } else {
                break;
            }
        }
        results.clear();
        results.addAll(bestRes);
    }

    private List<HyperErlangFitter> shuffle(HyperErlang dist, int numOfShuffles) {
        List<HyperErlangFitter> result = newArrayList();
        List<HyperErlangBranch> branches = dist.getBranches();
        RealMatrix relevance = new Array2DRowRealMatrix(samples.size(), branches.size());
        Map<Integer, Double> sumOfRow = Maps.newHashMap();
        for (int i = 0; i < samples.size(); i++) {
            for (int j = 0; j < branches.size(); j++) {
                relevance.setEntry(i, j, branches.get(j).dist.density(samples.getValue(i)));
            }
            sumOfRow.put(i, DoubleStream.of(relevance.getRow(i)).sum());
        }
        for (int i = 0; i < samples.size(); i++) {
            for (int j = 0; j < branches.size(); j++) {
                relevance.setEntry(i, j, relevance.getEntry(i, j) / sumOfRow.get(i));
            }
        }
        for (int i = 0; i < numOfShuffles; i++) {
            result.add(fitterFromCluster(discreteSamples(relevance)));
        }
        return result;
    }

    private HyperErlangFitter fitterFromCluster(List<SampleCollection> sampleCollections) {
        HyperErlangFitter res = new HyperErlangFitter(samples);
        res.setCluster(sampleCollections);
        res.branchFit();
        return res;
    }

    private void branchFit() {
        List<HyperErlangBranch> branches = newArrayList();
        for (SampleCollection sc : getCluster()) {
            branches.add(new HyperErlangBranch(sc.size() / (double) samples.size(),
                    (Erlang) FitterFactory.getFitterByName(MomErlangFitter.FITTER_NAME, sc).fit()));
        }
        fitResult = new HyperErlang(branches);
    }

    private List<SampleCollection> discreteSamples(RealMatrix relevance) {
        List<SampleCollection> res = newArrayList();
        Multimap<Integer, SampleItem> clusters = ArrayListMultimap.create();
        for (int i = 0; i < samples.size(); i++) {
            clusters.put(findCluster(i, relevance), samples.getSample(i));
        }
        for (int i = 0; i < relevance.getColumnDimension(); i++) {
            List<SampleItem> branch = newArrayList();
            branch.addAll(clusters.get(i));
            res.add(new SampleCollection(branch));
        }
        return res;
    }

    private int findCluster(int sampleIndex, RealMatrix relevance) {
        int i;
        double d = FastMath.random();
        double sum = 0;
        for (i = 0; i < relevance.getColumnDimension(); i++) {
            sum += relevance.getEntry(sampleIndex, i);
            if (sum > d) {
                break;
            }
        }
        return i == relevance.getColumnDimension() ? i - 1 : i;
    }


    /**
     * initial fit
     * cluster the samples by K-Means
     * and fit each cluster to an erlang distribution
     *
     * @return fitting result
     */
    private HyperErlang initFit() {
        List<ErlangFitter> fitters = newArrayList();
        int branch = FitParameters.getInstance().getBranch();
        KMeansPlusPlusClusterer<SampleItem> clusterer = new KMeansPlusPlusClusterer<>(branch);
        List<CentroidCluster<SampleItem>> clusterRes = clusterer.cluster(samples.getData());
        CentroidCluster<SampleItem> cluster;
        SampleCollection sc;
        // user k-means cluster samples
        for (int i = 0, n = clusterRes.size(); i < n; i++) {
            cluster = clusterRes.get(i);
            sc = new SampleCollection(cluster.getPoints());
            fitters.add(i, (ErlangFitter) FitterFactory.getFitterByName(MomErlangFitter.FITTER_NAME, sc));
        }
        // fit every group
        double initProbability = 0;
        List<HyperErlangBranch> branches = newArrayList();
        for (Fitter<Erlang> fitter : fitters) {
            initProbability = fitter.samples.size() / (double) samples.size();
            branches.add(new HyperErlangBranch(initProbability, fitter.fit()));
        }
        return new HyperErlang(branches);
    }

    @Override
    public String getName() {
        return FITTER_NAME;
    }

    List<SampleCollection> getCluster() {
        List<SampleCollection> res = newArrayList();
        res.addAll(cluster);
        return cluster;
    }

    private void setCluster(List<SampleCollection> cluster) {
        this.cluster = cluster;
    }

    private class HyperErlangComparator implements Comparator<HyperErlangFitter> {
        @Override
        public int compare(HyperErlangFitter o1, HyperErlangFitter o2) {
            return Double.compare(o2.logLikelihood(), o1.logLikelihood());
        }
    }
}

package fu.mi.fitting.fitters;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import fu.mi.fitting.distributions.Erlang;
import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.distributions.HyperErlangBranch;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.sample.SampleItem;
import org.apache.commons.math3.distribution.RealDistribution;
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

/**
 * Created by shangzhihao on 3/10/16.
 * <p>
 * this is used to fit Hyper Erlang distribution.
 * there are 3 steps for that:
 * 1) cluster samples using k-means algorithm
 * 2) fit each cluster’s samples with an Erlang distribution
 * 3) the assignment of samples to clusters is refined iteratively
 * until either convergence is reached or
 * a maximum number of 100 rounds has elapsed.
 */
public class HyperErlangFitter extends Fitter {

    public static final String FITTER_NAME = "Hyper-Erlang";
    private Logger logger = LoggerFactory.getLogger(HyperErlangFitter.class);
    private HyperErlang fitResult = null;
    private double llh = -1;


    private List<SampleCollection> cluster;

    HyperErlangFitter(SampleCollection sc) {
        super(sc);
    }

    @Override
    public RealDistribution fit() {
        if (fitResult == null) {
            HyperErlangFitter bestFitter = refinement(initFit());
            setCluster(bestFitter.getCluster());
            branchFit();
        }
        return fitResult;
    }

    @Override
    public double logLikelihood() {
        if (llh == -1) {
            HyperErlang dist = (HyperErlang) fit();
            llh = dist.logLikelihood(samples);
        }
        return llh;
    }

    /**
     * refine initial fitting result
     *
     * @param roughRes initial fitting result
     * @return refined result, this would be the final result
     */
    private HyperErlangFitter refinement(HyperErlang roughRes) {
        // TODO read form gui
        int maxCandidate = 10;
        int reassignment = 30;
        int numOfShuffles = 2;

        TreeSet<HyperErlangFitter> results = new TreeSet<>(new HyperErlangComparator());
        TreeSet<HyperErlangFitter> loopRes = new TreeSet<>(new HyperErlangComparator());
        List<HyperErlangFitter> resCandidate;
        results.addAll(shuffle(roughRes, numOfShuffles));
        for (int i = 0; i < reassignment; i++) {
            logger.info("the {}th reassign.", i);
            loopRes.clear();
            int visitedCandidate = 0;
            for (HyperErlangFitter fitter : results) {
                visitedCandidate++;
                if (visitedCandidate > maxCandidate) {
                    break;
                }
                resCandidate = shuffle((HyperErlang) fitter.fit(), numOfShuffles);
                logger.info("log likelihood: {}", fitter.logLikelihood());
                loopRes.addAll(resCandidate);
            }
            results.addAll(loopRes);
        }
        return results.first();
    }

    private List<HyperErlangFitter> shuffle(HyperErlang dist, int numOfShuffles) {
        List<HyperErlangFitter> result = Lists.newArrayList();
        List<HyperErlangBranch> branches = dist.branches;
        RealMatrix relevance = new Array2DRowRealMatrix(samples.data.size(), branches.size());
        Map<Integer, Double> sumOfRow = Maps.newHashMap();
        for (int i = 0; i < samples.data.size(); i++) {
            for (int j = 0; j < branches.size(); j++) {
                relevance.setEntry(i, j, branches.get(j).dist.density(samples.data.get(i).value));
            }
            sumOfRow.put(i, DoubleStream.of(relevance.getRow(i)).sum());
        }
        for (int i = 0; i < samples.data.size(); i++) {
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
        HyperErlang dist = new HyperErlang();
        for (SampleCollection sc : getCluster()) {
            dist.addBranch(sc.data.size() / (double) samples.data.size(),
                    (Erlang) FitterFactory.getFitterByName(MomErlangFitter.FITTER_NAME, sc).fit());
        }
        fitResult = dist;
    }

    private List<SampleCollection> discreteSamples(RealMatrix relevance) {
        List<SampleCollection> res = Lists.newArrayList();
        Multimap<Integer, SampleItem> clusters = ArrayListMultimap.create();
        for (int i = 0; i < samples.data.size(); i++) {
            clusters.put(findCluster(i, relevance), samples.data.get(i));
        }
        for (int i = 0; i < relevance.getColumnDimension(); i++) {
            List<SampleItem> branch = Lists.newArrayList();
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
        List<ErlangFitter> fitters = Lists.newArrayList();
        int branch = FitParameters.getInstance().getBranch();
        KMeansPlusPlusClusterer<SampleItem> clusterer = new KMeansPlusPlusClusterer<>(branch);
        List<CentroidCluster<SampleItem>> clusterRes = clusterer.cluster(samples.data);
        CentroidCluster<SampleItem> cluster;
        SampleCollection sc;
        HyperErlang res = new HyperErlang();
        // user k-means cluster samples
        for (int i = 0, n = clusterRes.size(); i < n; i++) {
            cluster = clusterRes.get(i);
            sc = new SampleCollection(cluster.getPoints());
            fitters.add(i, (ErlangFitter) FitterFactory.getFitterByName(MomErlangFitter.FITTER_NAME, sc));
        }
        // fit every group
        double initProbability = 0;
        for (Fitter<Erlang> fitter : fitters) {
            initProbability = fitter.samples.data.size() / (double) samples.data.size();
            res.addBranch(initProbability, fitter.fit());
        }
        return res;
    }

    @Override
    public String getName() {
        return FITTER_NAME;
    }

    public List<SampleCollection> getCluster() {
        return cluster;
    }

    public void setCluster(List<SampleCollection> cluster) {
        this.cluster = cluster;
    }

    private class HyperErlangComparator implements Comparator<HyperErlangFitter> {
        @Override
        public int compare(HyperErlangFitter o1, HyperErlangFitter o2) {
            return Double.compare(o2.logLikelihood(), o1.logLikelihood());
        }
    }
}

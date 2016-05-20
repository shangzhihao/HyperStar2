package fu.mi.fitting.fitters;

import com.google.common.collect.Lists;
import fu.mi.fitting.distributions.Erlang;
import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.distributions.HyperErlangBranch;
import fu.mi.fitting.parameters.FitParameters;
import fu.mi.fitting.sample.SampleCollection;
import fu.mi.fitting.sample.SampleItem;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by shangzhihao on 3/10/16.
 *
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
    public int branch = 2;
    public List<Erlang> erlangs = Lists.newArrayList();
    public List<ErlangFitter> fitters = Lists.newArrayList();
    Logger logger = LoggerFactory.getLogger(HyperErlang.class);
    private int maxLoop = 5000;
    private Class erlangFitterClass = MLEErlangFitter.class;

    HyperErlangFitter(SampleCollection sc) {
        super(sc);
    }

    @Override
    public RealDistribution fit() {
        branch = FitParameters.getInstance().getBranch();
        KMeansPlusPlusClusterer<SampleItem> clusterer = new KMeansPlusPlusClusterer<>(branch);
        List<CentroidCluster<SampleItem>> clusterRes = clusterer.cluster(samples.data);
        CentroidCluster<SampleItem> cluster;
        SampleCollection sc;
        // step 1
        // user k-means cluster samples
        for(int i=0, n=clusterRes.size(); i<n; i++){
            cluster = clusterRes.get(i);
            sc = new SampleCollection(cluster.getPoints());
            fitters.add(i, (ErlangFitter) FitterFactory.getFitterByName(MomErlangFitter.FITTER_NAME, sc));
        }
        // step 2
        // fit every group
        fitAll();
        int loop = 0;
        boolean noChange;
        int maxPdfIndex;
        // step 3
        // move sample to max pdf group and re-fit every distribution
        while(loop < maxLoop) {
            noChange = true;
            for(int i=0; i<fitters.size(); i++){
                for(int j=0; j<fitters.get(i).samples.data.size(); j++){
                    maxPdfIndex = findMaxPdf(i, j);
                    if(maxPdfIndex != i){
                        logger.debug("loop {} transfer sample {} from {} to {}",
                                loop, j, i, maxPdfIndex);
                        transferSample(j, i, maxPdfIndex);
                        //noChange = false;
                    }
                }
            }
            removeEmptyGroup();
            if(noChange)break;
            reFitAll();
            loop++;
        }
        assert erlangs.size() == fitters.size();
        // make result
        List<HyperErlangBranch> branches = Lists.newArrayList();
        double total = samples.data.size();
        for(int i = 0; i < erlangs.size(); i++){
            branches.add(new HyperErlangBranch(fitters.get(i).samples.data.size() / total, erlangs.get(i)));
        }
        HyperErlang res = new HyperErlang();
        res.branches = branches;
        return res;
    }

    @Override
    public String getName() {
        return FITTER_NAME;
    }

    /**
     * move sample to another group
     * @param index index of sample
     * @param source source group
     * @param dest destination group
     */
    private void transferSample(int index, int source, int dest) {
        SampleItem sample = fitters.get(source).samples.data.get(index);
        fitters.get(dest).samples.data.add(sample);
        fitters.get(source).samples.data.remove(index);
    }

    /**
     * find the index of fitter,
     * which maximize the pdf of sample
     *
     * @param f index of fitter
     * @param s index of sample
     * @return the index of fitter
     */
    private int findMaxPdf(int f, int s){
        double x = fitters.get(f).samples.data.get(s).value;
        Erlang dist;
        int res = f;
        double maxValue = -1;
        double temp;
        for(int i=0, n=fitters.size(); i<n; i++){
            dist = erlangs.get(i);
            temp = dist.density(x);
            if(temp > maxValue){
                maxValue = temp;
                res = i;
            }
        }
        return  res;
    }
    private void fitAll() {
        for (ErlangFitter fitter : fitters) {
            erlangs.add(fitter.fit());
        }
    }
    private void reFitAll(){
        for(int i=0, n=fitters.size(); i<n; i++){
            erlangs.set(i, fitters.get(i).fit());
        }
    }

    /**
     * remove groups which have no samples
     */
    private void removeEmptyGroup(){
        List<Integer> empty = Lists.newArrayList();
        for(int i=0;i<fitters.size();i++){
            if(fitters.get(i).samples.data.size() == 0){
                empty.add(i);
            }
        }
        for(int i:empty){
            fitters.remove(i);
            erlangs.remove(i);
        }
    }
}

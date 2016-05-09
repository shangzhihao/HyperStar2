package fu.mi.fitting.parameters;

import fu.mi.fitting.sample.SampleCollection;

/**
 * Created by shang on 5/9/2016.
 * store samples and related parameters
 */
public class SamplesParameters {
    private static final SamplesParameters INSTANCE = new SamplesParameters();
    private SampleCollection originSamples;
    private SampleCollection limitedSamples;
    // how many percents samples used in fitting
    private int size = 100;
    // sample range, samples in this range will be used in fitting
    // others samples will be ignored
    private double from;
    private double to;

    private SamplesParameters() {
    }

    public static SamplesParameters getInstance() {
        return INSTANCE;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size > 0 && size < 100) {
            this.size = size;
        }
    }

    public double getFrom() {
        return from;
    }

    public void setFrom(double from) {
        this.from = from;
    }

    public double getTo() {
        return to;
    }

    public void setTo(double to) {
        this.to = to;
    }

    public SampleCollection getOriginSamples() {
        return originSamples;
    }

    public void setOriginSamples(SampleCollection originSamples) {
        this.originSamples = originSamples;
        this.setLimitedSamples(originSamples.subSampleCollection(getSize()));
    }

    public SampleCollection getLimitedSamples() {
        return limitedSamples;
    }

    public void setLimitedSamples(SampleCollection limitedSamples) {
        this.limitedSamples = limitedSamples;
    }

}

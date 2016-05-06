package fu.mi.fitting.parameters;

/**
 * Created by shang on 5/6/2016.
 * store parameters for drawing chart
 */
public class ChartsParameters {
    private static final ChartsParameters INSTANCE = new ChartsParameters();
    // bins for histograms
    private int bins = 100;
    // steps for pdf drawing
    private int pdfSteps = 200;
    // steps for cdf drawing
    private int cdfSteps = 200;

    private ChartsParameters() {
    }

    public static ChartsParameters getInstance() {
        return INSTANCE;
    }

    // getters and setters
    public int getBins() {
        return bins;
    }

    public void setBins(int bins) {
        this.bins = bins;
    }

    public int getPdfSteps() {
        return pdfSteps;
    }

    public void setPdfSteps(int pdfSteps) {
        this.pdfSteps = pdfSteps;
    }

    public int getCdfSteps() {
        return cdfSteps;
    }

    public void setCdfSteps(int cdfSteps) {
        this.cdfSteps = cdfSteps;
    }
}

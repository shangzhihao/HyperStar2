package fu.mi.fitting.parameters;

/**
 * Created by shang on 5/6/2016.
 * store parameters for drawing chart
 */
public class ChartsParameters {
    private static final ChartsParameters INSTANCE = new ChartsParameters();
    // bins for histograms
    private int bins = 100;
    // number of points in pdf chart
    private int pdfPoints = 200;
    // number of points in cdf chart
    private int cdfPoints = 200;

    private int sampleSize;

    private double sampleFrom;
    private double sampleTo;
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

    public int getPdfPoints() {
        return pdfPoints;
    }

    public void setPdfPoints(int pdfPoints) {
        this.pdfPoints = pdfPoints;
    }

    public int getCdfPoints() {
        return cdfPoints;
    }

    public void setCdfPoints(int cdfPoints) {
        this.cdfPoints = cdfPoints;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        if (sampleSize < 0) {
            this.sampleSize = 0;
            return;
        }
        if (sampleSize > 100) {
            this.sampleSize = 100;
            return;
        }
        this.sampleSize = sampleSize;
    }

    public double getSampleFrom() {
        return sampleFrom;
    }

    public void setSampleFrom(double sampleFrom) {
        this.sampleFrom = sampleFrom;
    }

    public double getSampleTo() {
        return sampleTo;
    }

    public void setSampleTo(double sampleTo) {
        this.sampleTo = sampleTo;
    }
}

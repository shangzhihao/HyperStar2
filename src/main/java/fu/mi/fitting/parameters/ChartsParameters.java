package fu.mi.fitting.parameters;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import static fu.mi.fitting.utils.CommonUtils.strToInt;

/**
 * Created by shang on 5/6/2016.
 * store parameters for drawing chart
 */
public class ChartsParameters {
    private static final ChartsParameters INSTANCE = new ChartsParameters();
    private static final int DEFAULT_BINS = 200;
    private static final int DEFAULT_PDF = 300;
    private static final int DEFAULT_CDF = 300;
    private static final int DEFAULT_MOMENTS = 3;
    private StringProperty bins = new SimpleStringProperty(String.valueOf(DEFAULT_BINS));
    private StringProperty cdf = new SimpleStringProperty(String.valueOf(DEFAULT_CDF));
    private StringProperty pdf = new SimpleStringProperty(String.valueOf(DEFAULT_PDF));
    private StringProperty moments = new SimpleStringProperty(String.valueOf(DEFAULT_MOMENTS));

    private ChartsParameters() {
    }

    public static ChartsParameters getInstance() {
        return INSTANCE;
    }

    public StringProperty getBinsProperty() {
        return bins;
    }

    public StringProperty getCDFProperty() {
        return cdf;
    }

    public StringProperty getPDFProperty() {
        return pdf;
    }

    public StringProperty getMomentsProperty() {
        return moments;
    }

    public int getBins() {
        return strToInt(bins.get(), DEFAULT_BINS);
    }

    public int getPDFPoints() {
        return strToInt(pdf.get(), DEFAULT_PDF);
    }

    public int getCDFPoints() {
        return strToInt(cdf.get(), DEFAULT_CDF);
    }

    public int getMaxMomentOrder() {
        return strToInt(moments.get(), DEFAULT_MOMENTS);
    }
}

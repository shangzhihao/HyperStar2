package fu.mi.fitting.charts;

import fu.mi.fitting.controllers.ControllerResource;
import fu.mi.fitting.sample.SampleCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

/**
 * Created by shang on 5/6/2016.
 */
public class PDFChart {
    SampleCollection sc;
    JFreeChart histogram = ChartFactory.createHistogram(null, "x", "pdf(x)",
            sc.asHistogramDataset(""), PlotOrientation.VERTICAL,
            true, true, true);

    public PDFChart() {
        sc = ControllerResource.getInstance().mainController.getSampleCollection();
    }
}

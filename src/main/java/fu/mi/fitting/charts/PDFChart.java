package fu.mi.fitting.charts;

import fu.mi.fitting.controllers.ControllerResource;
import fu.mi.fitting.sample.SampleCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

/**
 * Created by shang on 5/6/2016.
 * PDF chart will be displayed in the PDF tab.
 */
public class PDFChart {
    // TODO make sure sc is not null
    SampleCollection sc;
    JFreeChart histogram = ChartFactory.createHistogram(null, "x", "histogram",
            sc.asHistogramDataset(""), PlotOrientation.VERTICAL,
            true, true, true);

    public PDFChart() {
        sc = ControllerResource.getInstance().mainController.getSampleCollection();
    }
}

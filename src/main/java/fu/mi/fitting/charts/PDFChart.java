package fu.mi.fitting.charts;

import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

/**
 * Created by shang on 5/6/2016.
 * this class is only used to draw pdf chart,
 * dataset and parameters come from ChartsController,
 * and ChartsController will show the result.
 */
public class PDFChart extends BaseChart {
    @Override
    public JFreeChart getChart(String chartName) {
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        chart = ChartFactory.createHistogram("", "samples", "histogram",
                sc.asHistogramDataset(chartName),
                PlotOrientation.VERTICAL,
                true, true, true);
        return chart;
    }
}

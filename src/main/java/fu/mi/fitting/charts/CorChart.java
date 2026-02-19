package fu.mi.fitting.charts;

import fu.mi.fitting.parameters.ChartsParameters;
import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Created by shang on 6/10/2016.
 * Correlation chart of MAP
 */
public class CorChart extends BaseChart {
    @Override
    public JFreeChart getChart(String chartName) {
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        final XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries corSeries = new XYSeries("Samples Correlation");
        int point = ChartsParameters.getInstance().getCorPoints();
        int maxLag = Math.min(point, sc.size() - 1);
        for (int i = 1; i <= maxLag; i++) {
            corSeries.add(i, sc.autocorrelation(i));
        }
        dataset.addSeries(corSeries);
        chart = ChartFactory.createXYLineChart("", "samples", "correlation", dataset);
        return chart;
    }
}

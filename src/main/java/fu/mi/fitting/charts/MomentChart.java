package fu.mi.fitting.charts;

import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * Created by shang on 5/6/2016.
 * this class is only used to draw moments chart,
 * dataset and parameters come from ChartsController,
 * and ChartsController will show the result.
 */
public class MomentChart {
    public JFreeChart getMomentChart() {
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        int maxMoment = SamplesParameters.getInstance().getMaxMomentOrder();
        final XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries momentSeries = new XYSeries("Moment");
        for (int i = 1; i <= maxMoment; i++) {
            momentSeries.add(i, sc.getMoment(i));
        }
        dataset.addSeries(momentSeries);
        return ChartFactory.createXYLineChart("", "samples", "moment", dataset);
    }
}

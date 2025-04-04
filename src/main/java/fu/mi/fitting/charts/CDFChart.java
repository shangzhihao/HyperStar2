package fu.mi.fitting.charts;

import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.Collections;
import java.util.List;

/**
 * Created by shang on 5/6/2016.
 * this class is only used to draw cdf chart,
 * dataset and parameters come from ChartsController,
 * and ChartsController will show the result.
 */
public class CDFChart extends BaseChart {

    @Override
    public JFreeChart getChart(String chartName) {
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        List<Double> sampleList = sc.asDoubleList();
        Collections.sort(sampleList);
        double probabilityPerSample = 1.0 / sampleList.size();
        final XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries cdfSeries = new XYSeries("Samples CDF");
        double probability;
        for (int i = 0; i < sampleList.size(); i++) {
            probability = probabilityPerSample * (i + 1);
            cdfSeries.add(sampleList.get(i), new Double(probability));
        }
        dataset.addSeries(cdfSeries);
        chart = ChartFactory.createXYLineChart("", "samples", "CDF", dataset);
        return chart;
    }
}

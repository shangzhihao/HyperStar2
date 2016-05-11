package fu.mi.fitting.charts;

import fu.mi.fitting.parameters.SamplesParameters;
import fu.mi.fitting.sample.SampleCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYDataset;

import java.awt.*;

/**
 * Created by shang on 5/6/2016.
 * this class is only used to draw pdf chart,
 * dataset and parameters come from ChartsController,
 * and ChartsController will show the result.
 */
public class PDFChart {
    private JFreeChart histogram;

    public JFreeChart getHistogram(String histogramKey) {
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        histogram = ChartFactory.createHistogram("", "samples", "histogram",
                sc.asHistogramDataset(histogramKey),
                PlotOrientation.VERTICAL,
                true, true, true);
        return histogram;
    }

    public void drawPDF(XYDataset pdfDataset) {
        SampleCollection sc = SamplesParameters.getInstance().getLimitedSamples();
        XYPlot xyPlot = histogram.getXYPlot();
        xyPlot.setDataset(1, pdfDataset);
        xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        StandardXYItemRenderer render = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        render.setSeriesPaint(1, Color.BLACK, true);
        xyPlot.setRenderer(1, render);
    }
}

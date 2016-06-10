package fu.mi.fitting.charts;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shang on 6/1/2016.
 */
public abstract class BaseChart {
    protected JFreeChart chart;
    private Logger logger = LoggerFactory.getLogger(BaseChart.class);

    public abstract JFreeChart getChart(String chartName);

    public void addLine(XYDataset cdfDataset) {
        if (chart == null) {
            logger.error("there is no chart.");
            return;
        }
        XYPlot xyPlot = chart.getXYPlot();
        xyPlot.setDataset(1, cdfDataset);
        xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        StandardXYItemRenderer render = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        xyPlot.setRenderer(1, render);
    }
}

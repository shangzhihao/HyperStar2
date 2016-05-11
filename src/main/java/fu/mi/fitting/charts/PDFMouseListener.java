package fu.mi.fitting.charts;

import fu.mi.fitting.controllers.ControllerResource;
import javafx.scene.input.MouseButton;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.fx.interaction.ChartMouseEventFX;
import org.jfree.chart.fx.interaction.ChartMouseListenerFX;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by shang on 5/10/2016.
 * listener for click on pdf chart,
 * it's used to add peak on pdf chart.
 */
public class PDFMouseListener implements ChartMouseListenerFX {
    /**
     * when user clicks on histogram chart,
     * add a peak and draw a line.
     *
     * @param event event information (never <code>null</code>).
     */
    @Override
    public void chartMouseClicked(ChartMouseEventFX event) {
        MouseButton mouseButton = event.getTrigger().getButton();
        if (mouseButton.compareTo(MouseButton.PRIMARY) == 0) {
            JFreeChart pdfHistogramChart = event.getChart();
            double position = getXofEvent(event);
            ValueMarker valueMarker = new ValueMarker(position);
            valueMarker.setPaint(Color.BLUE);
            XYPlot plot = (XYPlot) pdfHistogramChart.getPlot();
            plot.addDomainMarker(valueMarker);
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEventFX event) {
    }

    /**
     * get the x coordinate where user clicked
     *
     * @param event
     * @return
     */
    private double getXofEvent(ChartMouseEventFX event) {
        ChartViewer histogramViewer = ControllerResource.getInstance().chartsController.histogramViewer;
        Rectangle2D area = histogramViewer.getRenderingInfo().getPlotInfo().getDataArea();
        double x = event.getTrigger().getSceneX();
        XYPlot plot = event.getChart().getXYPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double res = xAxis.java2DToValue(x, area, plot.getDomainAxisEdge());
        return res;
    }
}

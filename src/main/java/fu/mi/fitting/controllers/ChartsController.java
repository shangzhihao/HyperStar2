package fu.mi.fitting.controllers;

import fu.mi.fitting.parameters.ChartsParameters;
import fu.mi.fitting.sample.SampleCollection;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

import java.awt.*;

/**
 * Created by shang on 5/6/2016.
 * chart controller, all chart is drawn by this class
 */
public class ChartsController {
    private final String PDF_LABEL = "pdf";
    @FXML
    Tab pdfTab;
    @FXML
    AnchorPane pdfPane;
    @FXML
    Tab cdfTab;
    @FXML
    AnchorPane cdfPane;
    @FXML
    Tab momTab;
    @FXML
    AnchorPane momPane;
    private ControllerResource controllerResource = ControllerResource.getInstance();
    private ChartsParameters chartsParameters = ChartsParameters.getInstance();
    private SampleCollection sampleCollection;
    private JFreeChart histogram;

    @FXML
    public void initialize() {
        ControllerResource.getInstance().chartsController = this;
    }

    public void drawChart() {
        sampleCollection = controllerResource.mainController.getSampleCollection();
        drawHistogram();
        drawCDF();
        drawMoment();
    }

    private void drawMoment() {

    }

    private void drawCDF() {

    }

    private void drawHistogram() {
        histogram = ChartFactory.createHistogram(null, "samples", "histogram",
                sampleCollection.asHistogramDataset(getHistogramKey()),
                PlotOrientation.VERTICAL,
                true, true, true);
        ChartViewer viewer = new ChartViewer(histogram);
        pdfPane.getChildren().add(viewer);
        AnchorPane.setBottomAnchor(viewer, 0.0);
        AnchorPane.setTopAnchor(viewer, 0.0);
        AnchorPane.setLeftAnchor(viewer, 0.0);
        AnchorPane.setRightAnchor(viewer, 0.0);
    }

    private String getHistogramKey() {
        return "samples-" + chartsParameters.getBins() + " steps";
    }

    public void addPDF(Function2D pdf, double start, double end) {
        XYPlot xyPlot = histogram.getXYPlot();
        XYDataset pdfDataset = DatasetUtilities.sampleFunction2D(pdf, start, end,
                chartsParameters.getPdfSteps(), PDF_LABEL);
        xyPlot.setDataset(1, pdfDataset);
        xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        StandardXYItemRenderer render = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        render.setSeriesPaint(1, Color.green, true);
        xyPlot.setRenderer(1, render);
    }
}

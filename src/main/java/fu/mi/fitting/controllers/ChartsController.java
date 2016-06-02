package fu.mi.fitting.controllers;

import fu.mi.fitting.charts.BaseChart;
import fu.mi.fitting.charts.CDFChart;
import fu.mi.fitting.charts.PDFChart;
import fu.mi.fitting.charts.PDFMouseListener;
import fu.mi.fitting.parameters.ChartsParameters;
import fu.mi.fitting.parameters.Messages;
import fu.mi.fitting.sample.SampleCollection;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

/**
 * Created by shang on 5/6/2016.
 * chart controller, all chart is drawn by this class
 */
public class ChartsController {
    public ChartViewer histogramViewer;
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
    private ChartsParameters chartsParameters = ChartsParameters.getInstance();
    private SampleCollection sampleCollection;
    private BaseChart pdfChart = new PDFChart();
    private BaseChart cdfChart = new CDFChart();

    @FXML
    public void initialize() {
        Controllers.getInstance().chartsController = this;
    }

    public void drawChart() {
        Controllers.getInstance().mainController.setStatus(Messages.DRAW_HISTOGRAM);
        drawHistogram();
        Controllers.getInstance().mainController.setStatus(Messages.DRAW_CDF);
        drawCDF();
        Controllers.getInstance().mainController.setStatus(Messages.NONE_STATUS);
    }


    private void drawCDF() {
        JFreeChart cdfLineChart = cdfChart.getChart("");
        ChartViewer viewer = new ChartViewer(cdfLineChart);
        cdfPane.getChildren().add(viewer);
        AnchorPane.setBottomAnchor(viewer, 0.0);
        AnchorPane.setTopAnchor(viewer, 0.0);
        AnchorPane.setLeftAnchor(viewer, 0.0);
        AnchorPane.setRightAnchor(viewer, 0.0);
    }

    private void drawHistogram() {
        JFreeChart histogram = pdfChart.getChart(getHistogramKey());
        histogramViewer = new ChartViewer(histogram);
        histogramViewer.addChartMouseListener(new PDFMouseListener());
        pdfPane.getChildren().add(histogramViewer);
        AnchorPane.setBottomAnchor(histogramViewer, 0.0);
        AnchorPane.setTopAnchor(histogramViewer, 0.0);
        AnchorPane.setLeftAnchor(histogramViewer, 0.0);
        AnchorPane.setRightAnchor(histogramViewer, 0.0);
    }

    private String getHistogramKey() {
        return "samples-" + chartsParameters.getBins() + " steps";
    }

    void addPDF(Function2D pdf, double start, double end) {
        XYDataset pdfDataset = DatasetUtilities.sampleFunction2D(pdf, start, end,
                chartsParameters.getPDFPoints(), Messages.PDF_LABEL);
        pdfChart.drawLine(pdfDataset);
    }

    void addCDF(Function2D cdf, double start, double end) {
        XYDataset pdfDataset = DatasetUtilities.sampleFunction2D(cdf, start, end,
                chartsParameters.getCDFPoints(), Messages.PDF_LABEL);
        cdfChart.drawLine(pdfDataset);
    }
}

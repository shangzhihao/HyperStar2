package fu.mi.fitting.controllers;

import fu.mi.fitting.charts.*;
import fu.mi.fitting.distributions.MarkovArrivalProcess;
import fu.mi.fitting.parameters.ChartsParameters;
import fu.mi.fitting.parameters.Messages;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Created by shang on 5/6/2016.
 * chart controller, all chart is drawn by this class
 */
public class ChartsController {
    public ChartViewer histogramViewer;
    @FXML
    TabPane chartsPane;
    @FXML
    Tab pdfTab;
    @FXML
    AnchorPane pdfPane;
    @FXML
    Tab cdfTab;
    @FXML
    AnchorPane cdfPane;
    @FXML
    Tab corTab;
    @FXML
    AnchorPane corPane;
    private ChartsParameters chartsParameters = ChartsParameters.getInstance();
    private BaseChart pdfChart = new PDFChart();
    private BaseChart cdfChart = new CDFChart();
    private BaseChart corChart = new CorChart();

    @FXML
    public void initialize() {
        Controllers.getInstance().chartsController = this;
        chartsPane.getTabs().remove(corTab);
    }

    public void drawChart() {
        Controllers.getInstance().mainController.setStatus(Messages.DRAW_HISTOGRAM);
        drawHistogram();
        Controllers.getInstance().mainController.setStatus(Messages.DRAW_CDF);
        drawCDF();
        Controllers.getInstance().mainController.setStatus(Messages.NONE_STATUS);
    }

    private void drawCDF() {
        drawLine(cdfChart, cdfPane);
    }

    public void drawCorrelation() {
        drawLine(corChart, corPane);
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

    private void drawLine(BaseChart chart, AnchorPane pane) {
        JFreeChart correlation = chart.getChart("");
        ChartViewer viewer = new ChartViewer(correlation);
        pane.getChildren().add(viewer);
        AnchorPane.setBottomAnchor(viewer, 0.0);
        AnchorPane.setTopAnchor(viewer, 0.0);
        AnchorPane.setLeftAnchor(viewer, 0.0);
        AnchorPane.setRightAnchor(viewer, 0.0);
    }

    private String getHistogramKey() {
        return "samples-" + chartsParameters.getBins() + " steps";
    }

    void addPDF(Function2D pdf, double start, double end) {
        XYDataset pdfDataset = DatasetUtilities.sampleFunction2D(pdf, start, end,
                chartsParameters.getPDFPoints(), Messages.PDF_LABEL);
        pdfChart.addLine(pdfDataset);
    }

    void addCDF(Function2D cdf, double start, double end) {
        XYDataset pdfDataset = DatasetUtilities.sampleFunction2D(cdf, start, end,
                chartsParameters.getCDFPoints(), Messages.PDF_LABEL);
        cdfChart.addLine(pdfDataset);
    }

    void addCorrelation(MarkovArrivalProcess map) {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries corSeries = new XYSeries("Correlation");
        int point = ChartsParameters.getInstance().getCorPoints();
        for (int i = 1; i <= point; i++) {
            corSeries.add(i, map.autoCorrelation(i));
        }
        dataset.addSeries(corSeries);
        corChart.addLine(dataset);
    }
}

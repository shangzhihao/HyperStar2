package fu.mi.fitting.controller;

import fu.mi.fitting.distributions.HyperErlang;
import fu.mi.fitting.domains.FitParameter;
import fu.mi.fitting.domains.SampleCollection;
import fu.mi.fitting.fitters.HyperErlangFitter;
import fu.mi.fitting.io.LineSampleReader;
import fu.mi.fitting.io.SampleReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;

import java.io.File;

/**
 * Created by shang on 5/1/2016.
 */
public class MainController {

    @FXML Tab pdfTab;
    @FXML TableView argsTable;
    @FXML TableColumn valueCol;

    SampleCollection sc;
    private int branch = 5;
    private int bins = 500;
    private JFreeChart histogram;

    public void fitDistribution(ActionEvent actionEvent) {
        ObservableList<FitParameter> items = argsTable.getItems();
        for(FitParameter item: items){
            if(item.getKey().equals("branch")){
                branch = Integer.parseInt(item.getValue());
                break;
            }
        }
        HyperErlangFitter herFitter = new HyperErlangFitter(sc);
        herFitter.branch = branch;
        HyperErlang res = (HyperErlang) herFitter.fit();
        XYPlot xyPlot = histogram.getXYPlot();
        Function2D pdf = d -> res.density(d);
        double start = StatUtils.min(sc.asDoubleArray());
        double end = StatUtils.max(sc.asDoubleArray());
        XYDataset pdfDataset = DatasetUtilities.sampleFunction2D(pdf, start, end, 300, "pdf");
        xyPlot.setDataset(1, pdfDataset);
        xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        StandardXYItemRenderer s = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        xyPlot.setRenderer(1, s);
    }
    @FXML
    public void initialize(){
        final ObservableList<FitParameter> items = FXCollections.observableArrayList(
                new FitParameter("branch", "5"),
                new FitParameter("steps", "100"),
                new FitParameter("pdf samples", "300")
        );
        argsTable.setItems(items);
        Callback<TableColumn<FitParameter, String>, TableCell<FitParameter, String>> cellFactory
                = (TableColumn<FitParameter, String> p) -> new EditingCell();
        valueCol.setCellFactory(cellFactory);
        valueCol.setOnEditCommit(
                event -> {
                    TableColumn.CellEditEvent<FitParameter, String> e = (TableColumn.CellEditEvent<FitParameter, String>)event;
                    int row = e.getTablePosition().getRow();
                    ObservableList<FitParameter> parameters = e.getTableView().getItems();
                    parameters.get(row).setValue(e.getNewValue());
                    //e.getTableView().setItems(parameters);
                }
        );
    }

    public void loadSamples(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File inputFile = fileChooser.showOpenDialog(GuiResource.stage);
        if(inputFile == null){
            return;
        }
        SampleReader sr = new LineSampleReader(inputFile);
        sc = sr.read();

        AnchorPane pdfPane = (AnchorPane) pdfTab.getContent();
        final SwingNode chartSwingNode = new SwingNode();
        HistogramDataset sampleHistogramDataset = new HistogramDataset();
        sampleHistogramDataset.setType(HistogramType.SCALE_AREA_TO_1);

        ObservableList<FitParameter> items = argsTable.getItems();
        for(FitParameter item: items){
            if(item.getKey().equals("key")){
                bins = Integer.parseInt(item.getValue());
                break;
            }
        }
        sampleHistogramDataset.addSeries("samples-"+bins+"steps",sc.asDoubleArray(), bins);
        chartSwingNode.setContent(
            new ChartPanel(
                generatePieChart(sampleHistogramDataset)
            )
        );
        pdfPane.getChildren().add(chartSwingNode);
        AnchorPane.setBottomAnchor(chartSwingNode, 0.0);
        AnchorPane.setTopAnchor(chartSwingNode, 0.0);
        AnchorPane.setLeftAnchor(chartSwingNode, 0.0);
        AnchorPane.setRightAnchor(chartSwingNode, 0.0);
    }
    private JFreeChart generatePieChart(IntervalXYDataset sampleHistogramDataset) {
        histogram = ChartFactory.createHistogram(null, "x", "pdf(x)",
                sampleHistogramDataset, PlotOrientation.VERTICAL,
                true,  true, true);
        return histogram;
    }
}

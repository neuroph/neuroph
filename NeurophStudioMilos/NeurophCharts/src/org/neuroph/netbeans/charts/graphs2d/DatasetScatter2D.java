package org.neuroph.netbeans.charts.graphs2d;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.nugs.graph2d.JFreeScatter2DFactory;
import org.nugs.graph2d.api.Graph2DProperties;
import org.nugs.graph2d.api.Scatter2DFactory;
import org.neuroph.netbeans.charts.providers2d.DatasetDataProvider2D;

/**
 *
 * @author Vedrana Gajic
 */
public class DatasetScatter2D extends Graph2DBuilder {

    private Point2D[] data;
    private final DataSet dataset;
    ArrayList<String> patterns;

    public DatasetScatter2D(DataSet dataset) {
        provider2D = new DatasetDataProvider2D(dataset);
        this.dataset = dataset;
        fillPatterns(dataset);

    }

    @Override
    public String toString() {
        return "Scatter 2D";
    }

    @Override
    public ChartPanel create() {
        data = provider2D.getData(attribute1, attribute2);
        Graph2DProperties prop = new Graph2DProperties("Dataset visualization", attribute1.getLabel(), attribute2.getLabel());
        prop.setTooltipsVisible(true);
        int size = patterns.size();
        Paint[] boje = {Color.RED, Color.BLUE, Color.ORANGE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.GRAY, Color.MAGENTA, Color.BLACK, Color.PINK};
        boje = fillColors(boje, size);
        prop.setSeriesColors(boje);


        XYSeries[] serije = new XYSeries[size];
        for (int i = 0; i < serije.length; i++) {
            serije[i] = new XYSeries("Serija " + i + 1);
            serije[i].setDescription("Serija " + i + 1);
        }
        fillSerie(serije);

        Scatter2DFactory<ChartPanel> scatter2DFactory = new JFreeScatter2DFactory();
        ChartPanel chartPanel = scatter2DFactory.createScatter2D(serije, prop);
        return chartPanel;
    }

    private void fillSerie(XYSeries[] xySeries) {
        for (int i = 0; i < dataset.getRows().size(); i++) {
            DataSetRow row = dataset.getRowAt(i);
            for (int j = 0; j < patterns.size(); j++) {
                String outputs = Arrays.toString(row.getDesiredOutput());
                if (outputs.equals(patterns.get(j)) && data[i] != null) {
                    xySeries[j].add(data[i].getX(), data[i].getY());
                    break;
                }
            }
        }
    }

    private void fillPatterns(DataSet dataset) {
        patterns = new ArrayList<String>();
        for (int i = 0; i < dataset.getRows().size(); i++) {
            DataSetRow row = dataset.getRowAt(i);
            double[] outputs = row.getDesiredOutput();
            String out = Arrays.toString(outputs);

            if (!patterns.contains(out)) {
                patterns.add(out);
            }

        }
    }

    private Paint[] fillColors(Paint[] boje, int size) {

        if (size > 10) {
            boje = new Paint[size];
            boje[0] = Color.RED;
            boje[1] = Color.BLUE;
            boje[2] = Color.ORANGE;
            boje[3] = Color.GREEN;
            boje[4] = Color.YELLOW;
            boje[5] = Color.CYAN;
            boje[6] = Color.GRAY;
            boje[7] = Color.MAGENTA;
            boje[8] = Color.BLACK;
            boje[9] = Color.PINK;
            for (int i = 10; i < size; i++) {
                int R = (int) (Math.random() * 256);
                int G = (int) (Math.random() * 256);
                int B = (int) (Math.random() * 256);
                boje[i] = new Color(R, G, B);
            }
        }
        return boje;


    }
}

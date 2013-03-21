package org.nugs.graph3d;

import java.beans.Beans;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.HistogramBar;
import org.jzy3d.plot3d.rendering.scene.Scene;

/**
 *
 * @author vedrana
 */
public class Hist3DFactory {

    /**
     * Create a histogram 3D chart
     *
     * @param heights heights of the bars
     * @param prop properties
     * @return Chart
     */
    public static Chart createHistogram(double[][] heights, Hist3DProperties prop) {
        Chart chart = new Chart(prop.getChartQuality());
        Scene scene = chart.getScene();
        Beans.setDesignTime(false);
        for (int row = 0; row < heights.length; row++) {
            for (int col = 0; col < heights[row].length; col++) {
                //addBar() metoda
                IColorMap map = prop.getChartColor();
                map.setDirection(false);
                ColorMapper mapper = new ColorMapper(map, -1, 1);
                Color color = mapper.getColor(new Coord3d(0, 0, heights[row][col]));
                color.a = 0.55f;
                HistogramBar bar = new HistogramBar();

                //najbitnije
                bar.setData(new Coord3d(row, col, 0), (float) heights[row][col], prop.getRadius(), color);
                bar.setWireframeDisplayed(prop.isChartWireframed()); //ovo mora posle setData
                bar.setWireframeColor(prop.getWireframeColor()); //ovo mora posle setData
                scene.add(bar);             
            }
        }
        return chart;
    }

    /**
     * Create a histogram 3D chart with default properties
     *
     * @param heights heights of the bars
     * @return Chart
     */
    public static Chart createHistogram(double[][] heights) {
        Chart chart = createHistogram(heights, new Hist3DProperties());
        return chart;
    }
}

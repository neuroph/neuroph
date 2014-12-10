package org.nugs.graph3d;

import org.nugs.graph3d.api.Scatter3DProperties;
import org.nugs.graph3d.api.Point3D;
import java.beans.Beans;
import javax.media.opengl.GLCapabilities;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory.Toolkit;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.ScatterMultiColor;
//import org.jzy3d.plot3d.primitives.MultiColorScatter;
import org.jzy3d.plot3d.primitives.axes.layout.providers.RegularTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.IntegerTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.nugs.graph3d.api.Scatter3DFactory;

/**
 *
 * @author Vedrana Gajic
 */
public class JzyScatter3DFactory implements Scatter3DFactory<Chart,Point3D> {

    /**
     * Create a scatter chart and open it on Chart launcher
     *
     * @param points all points of the chart
     * @param prop properties
     * @return void
     */
    @Override
    public Chart createScatter3D(Point3D[] points, Scatter3DProperties prop) {
        Beans.setDesignTime(false);
        // Create  points used by jzy3d
        Coord3d[] data = new Coord3d[points.length];
        // iterate  all input points and create Coord3d points
        for (int i = 0; i < points.length; i++) {
            data[0] = new Coord3d((double) points[1].getX(), (double) points[1].getY(), (double) points[1].getZ()); // whats this?

            if (points[i] != null) { // make sure that current point is not null
                double x = (double) points[i].getX();
                double y = (double) points[i].getY();
                double z = (double) points[i].getZ();
                data[i] = new Coord3d(x, y, z); // and convert it to point used by Coord3d
            }
        }

        // Create a drawable scatter with a colormap
        IColorMap map = prop.getChartColor();
       // Chart chart = new Chart(prop.getChartQuality(), Toolkit.newt.name());

        
	Quality quality = new Quality(true, true, true, true, true, true, false);
        GLCapabilities capabilities = org.jzy3d.chart.Settings.getInstance().getGLCapabilities();
        capabilities.setSampleBuffers(true); // false = ragged edges around large points. true = smooth rounding.        
        AWTChartComponentFactory accf = new AWTChartComponentFactory();
        
        Chart chart = new Chart( accf,  quality, /*"awt"*/ Toolkit.newt.name(), capabilities);
        
        // AWTChartComponentFactory accf = new AWTChartComponentFactory();
        // Chart chart = accf.newChart(prop.getChartQuality() /*Quality.Nicest*/, Toolkit.newt.name());      
        
        Scene scene = chart.getScene();
        map.setDirection(true);
        ColorMapper mapper = new ColorMapper(map, scene.getGraph().getBounds().getZmin(), scene.getGraph().getBounds().getZmax());
      
        ScatterMultiColor scatter = new ScatterMultiColor(data, mapper);
        scatter.setWidth(prop.getDotSize());

        // Create a chart and add surface
        chart.getAxeLayout().setXAxeLabel(prop.getXAxeLabel());
        chart.getAxeLayout().setYAxeLabel(prop.getYAxeLabel());
        chart.getAxeLayout().setZAxeLabel(prop.getZAxeLabel());
        if (prop.isXAxeInteger()) {
            chart.getAxeLayout().setXTickRenderer(new IntegerTickRenderer());
            int xMax = (int) prop.getxRange().getMax();
            if (xMax < 20) {
                chart.getAxeLayout().setXTickProvider(new StaticTickProvider(getRowsOfData(xMax)));
            } else {
                chart.getAxeLayout().setXTickProvider(new RegularTickProvider(20));
            }
        }
        if (prop.isYAxeInteger()) {
            chart.getAxeLayout().setYTickRenderer(new IntegerTickRenderer());
            int yMax = (int) prop.getyRange().getMax();
            if (yMax < 20) {
                chart.getAxeLayout().setYTickProvider(new StaticTickProvider(getRowsOfData(yMax)));
            } else {
                chart.getAxeLayout().setYTickProvider(new RegularTickProvider(20));
            }
        }

        chart.getScene().add(scatter);
        return chart;
    }

    /**
     * Create a scatter chart with default properties and open it on Chart
     * launcher
     *
     * @param points all points of the chart
     * @return void
     */
    @Override
    public Chart createScatter3D(Point3D[] points) {
        return createScatter3D(points, new Scatter3DProperties());
    }

    private static double[] getRowsOfData(int count) {
        double[] rows = new double[count];

        for (int i = 0; i < count; i++) {
            rows[i] = i + 1;
        }
        return rows;

    }
}

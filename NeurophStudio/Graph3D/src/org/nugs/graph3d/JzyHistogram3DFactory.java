package org.nugs.graph3d;

import org.nugs.graph3d.api.Histogram3DProperties;
import org.nugs.graph3d.api.Point3D;
import java.beans.Beans;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.HistogramBar;
import org.jzy3d.plot3d.primitives.axes.layout.providers.RegularTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.providers.SmartTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.IntegerTickRenderer;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.nugs.graph3d.api.Histogram3DFactory;

/**
 *
 * @author Vedrana Gajic
 */
public class JzyHistogram3DFactory implements Histogram3DFactory<Chart,Point3D> {

    /**
     * Create a histogram 3D chart
     *
     * @param points heights of the bars
     * @param prop properties
     * @return Chart
     */
    @Override
    public Chart createHistogram3D(Point3D[] points, Histogram3DProperties prop) {
        Chart chart = new Chart(prop.getChartQuality());
        Scene scene = chart.getScene();
        Beans.setDesignTime(false);
        for (int row = 0; row < points.length; row++) {
            if (points[row] != null) {
                IColorMap map = prop.getChartColor();
                map.setDirection(true);
                ColorMapper mapper = new ColorMapper(map, scene.getGraph().getBounds().getZmin(), scene.getGraph().getBounds().getZmax());
                Color color = mapper.getColor(new Coord3d(0, 0, points[row].getZ()));
                color.a = 0.55f;
                HistogramBar bar = new HistogramBar();

                //setData(Coord3d position, float height, float radius, Color color) 
                bar.setData(new Coord3d(points[row].getX(), points[row].getY(), 0), (float) points[row].getZ(), prop.getRadius(), color);
                bar.setWireframeDisplayed(prop.isChartWireframed()); //ovo mora posle setData
              
                bar.setWireframeColor(prop.getWireframeColor()); //ovo mora posle setData

                scene.add(bar);
            }
        }
        chart.getAxeLayout().setXAxeLabel(prop.getxAxeLabel());
        chart.getAxeLayout().setYAxeLabel(prop.getyAxeLabel());
        chart.getAxeLayout().setZAxeLabel(prop.getzAxeLabel());
        if (prop.isxAxeInteger()) {
            chart.getAxeLayout().setXTickRenderer(new IntegerTickRenderer());
        }
        if (prop.isyAxeInteger()) {
            chart.getAxeLayout().setYTickRenderer(new IntegerTickRenderer());
        }

        if (points[points.length - 1].getX() < 20) {
            chart.getAxeLayout().setXTickProvider(new StaticTickProvider(getRowsOfData(points)));
        } else {
            //preko 20 layera
            chart.getAxeLayout().setXTickProvider(new RegularTickProvider(20));
        }

        //int rows = points[0].length;
        int rows = (int) scene.getGraph().getBounds().getYmax();
        if (rows < 20) {
            //Ako ih je manje od 20 neka koraka bude onoliko koliko ih ima
            chart.getAxeLayout().setYTickProvider(new SmartTickProvider(rows));
        } else {
            //Ako je vise od 20 neka koraka bude ukupno 20
            chart.getAxeLayout().setYTickProvider(new RegularTickProvider(20));
        }

        return chart;
    }

    /**
     * Create a histogram 3D chart with default properties
     *
     * @param points
     * @return Chart
     */
    @Override
    public Chart createHistogram3D(Point3D[] points) {
        Chart chart = createHistogram3D(points, new Histogram3DProperties());
        return chart;
    }

    private static float[] getRowsOfData(Point3D[] points) {
        int l = (int) points[points.length - 1].getX();
        float[] rows = new float[l];

        for (int i = 0; i < l; i++) {
            rows[i] = i + 1;
        }
        return rows;
    }
}

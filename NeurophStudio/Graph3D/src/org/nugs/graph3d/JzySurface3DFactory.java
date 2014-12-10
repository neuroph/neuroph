package org.nugs.graph3d;

import org.nugs.graph3d.api.Surface3DProperties;
import org.nugs.graph3d.api.Point3D;
import java.beans.Beans;
import java.util.LinkedList;
import java.util.List;
import javax.media.opengl.GLCapabilities;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.layout.providers.RegularTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.IntegerTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.nugs.graph3d.api.Surface3DFactory;

/**
 *
 * @author Vedrana Gajic
 */
public class JzySurface3DFactory implements Surface3DFactory<Chart> {

    /**
     * Create a surface chart and open it on Chart launcher
     *
     * @param mapper function of chart
     * @param prop properties
     * @return void
     */
    @Override
    public Chart createSurface(Mapper mapper, Surface3DProperties prop) {
        Beans.setDesignTime(false);

        org.jzy3d.maths.Range xRange = new Range(prop.getXRange().getMin(), prop.getXRange().getMax() );
        org.jzy3d.maths.Range yRange = new Range(prop.getYRange().getMin(), prop.getYRange().getMax());
        
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(xRange, prop.getXSteps(), yRange, prop.getYSteps()), mapper);
        surface.setColorMapper(new ColorMapper(prop.getChartColor(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(prop.isChartWireframed());
        surface.setWireframeColor(prop.getWireframeColor());

        // Create a chart and add surface
       // Chart chart = new Chart(prop.getChartQuality());
        	Quality quality = new Quality(true, true, true, true, true, true, false);
        GLCapabilities capabilities = org.jzy3d.chart.Settings.getInstance().getGLCapabilities();
        capabilities.setSampleBuffers(true); // false = ragged edges around large points. true = smooth rounding.        
        AWTChartComponentFactory accf = new AWTChartComponentFactory();
        
        Chart chart = new Chart( accf,  quality, /*"awt"*/ IChartComponentFactory.Toolkit.newt.name(), capabilities);
        
        
        chart.getAxeLayout().setXAxeLabel(prop.getXAxeLabel());
        chart.getAxeLayout().setYAxeLabel(prop.getYAxeLabel());
        chart.getAxeLayout().setZAxeLabel(prop.getZAxeLabel());
        if (prop.isXAxeInteger()) {
            chart.getAxeLayout().setXTickRenderer(new IntegerTickRenderer());
            int xMax = (int) prop.getXRange().getMax();
            if (xMax < 20) {
                chart.getAxeLayout().setXTickProvider(new StaticTickProvider(getRowsOfData(xMax)));
            } else {
                //preko 20 layera
                chart.getAxeLayout().setXTickProvider(new RegularTickProvider(20));
            }
        }
        if (prop.isYAxeInteger()) {
            chart.getAxeLayout().setYTickRenderer(new IntegerTickRenderer());
            int yMax = (int) prop.getYRange().getMax();
            if (yMax < 20) {
                //Ako ih je manje od 20 neka koraka bude onoliko koliko ih ima
                chart.getAxeLayout().setYTickProvider(new StaticTickProvider(getRowsOfData(yMax)));
            } else {
                //Ako je vise od 20 neka koraka bude ukupno 20
                chart.getAxeLayout().setYTickProvider(new RegularTickProvider(20));
            }
        }



        chart.getScene().getGraph().add(surface);
        return chart;
    }

    /**
     * Create a surface chart with default properties and open it on Chart
     * launcher
     *
     * @param mapper function of chart
     * @return void
     */
    public Chart createSurface(Mapper mapper) {
        return createSurface(mapper, new Surface3DProperties());
    }

    /**
     * Creates a surface chart and open it on Chart launcher
     *
     * @param weights coordinates of the chart
     * @param prop properties
     * @return void
     */
    public Chart createSurface(Point3D[] weights, Surface3DProperties prop) {
        Beans.setDesignTime(false);
        List<Coord3d> lista = new LinkedList<>();
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] != null) {
                lista.add(new Coord3d(weights[i].getX(), weights[i].getY(), weights[i].getZ()));
            }
        }

        final Shape surface = Builder.buildDelaunay(lista);
        surface.setColorMapper(new ColorMapper(prop.getChartColor(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(prop.isChartWireframed());
        surface.setWireframeColor(prop.getWireframeColor());

        // Create a chart and add surface
        Chart chart = new Chart(prop.getChartQuality());
        chart.getAxeLayout().setXAxeLabel(prop.getXAxeLabel());
        chart.getAxeLayout().setYAxeLabel(prop.getYAxeLabel());
        chart.getAxeLayout().setZAxeLabel(prop.getZAxeLabel());
        if (prop.isXAxeInteger()) {
            chart.getAxeLayout().setXTickRenderer(new IntegerTickRenderer());
            int xMax = (int) prop.getXRange().getMax();
            if (xMax < 20) {
                chart.getAxeLayout().setXTickProvider(new StaticTickProvider(getRowsOfData(xMax)));
            } else {
                //preko 20 layera
                chart.getAxeLayout().setXTickProvider(new RegularTickProvider(20));
            }
        }
        if (prop.isYAxeInteger()) {
            chart.getAxeLayout().setYTickRenderer(new IntegerTickRenderer());
            int yMax = (int) prop.getYRange().getMax();
            if (yMax < 20) {
                //Ako ih je manje od 20 neka koraka bude onoliko koliko ih ima
                chart.getAxeLayout().setYTickProvider(new StaticTickProvider(getRowsOfData(yMax)));
            } else {
                //Ako je vise od 20 neka koraka bude ukupno 20
                chart.getAxeLayout().setYTickProvider(new RegularTickProvider(20));
            }
        }

        chart.getScene().getGraph().add(surface);
        return chart;
    }

    /**
     * Create a surface chart with default properties and open it on Chart
     * launcher
     *
     * @param points coordinates of the chart (x,y,z)
     * @return void
     */
    @Override
    public Chart createSurface(Point3D[] points) {
        return createSurface(points, new Surface3DProperties());
    }

    private static double[] getRowsOfData(int count) {
        double[] rows = new double[count];

        for (int i = 0; i < count; i++) {
            rows[i] = i + 1;
        }
        return rows;
    }

}

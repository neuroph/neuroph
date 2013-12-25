package org.nugs.graph3d;

import org.nugs.graph3d.api.Surface3DProperties;
import org.nugs.graph3d.api.Point3D;
import java.beans.Beans;
import java.util.LinkedList;
import java.util.List;
import org.jzy3d.chart.Chart;
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

        org.jzy3d.maths.Range xRange = new Range(prop.getxRange().getMin(), prop.getxRange().getMax() );
        org.jzy3d.maths.Range yRange = new Range(prop.getyRange().getMin(), prop.getyRange().getMax());
        
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(xRange, prop.getxSteps(), yRange, prop.getySteps()), mapper);
        surface.setColorMapper(new ColorMapper(prop.getChartColor(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(prop.isChartWireframed());
        surface.setWireframeColor(prop.getWireframeColor());

        // Create a chart and add surface
        Chart chart = new Chart(prop.getChartQuality());
        chart.getAxeLayout().setXAxeLabel(prop.getxAxeLabel());
        chart.getAxeLayout().setYAxeLabel(prop.getyAxeLabel());
        chart.getAxeLayout().setZAxeLabel(prop.getzAxeLabel());
        if (prop.isxAxeInteger()) {
            chart.getAxeLayout().setXTickRenderer(new IntegerTickRenderer());
            int xMax = (int) prop.getxRange().getMax();
            if (xMax < 20) {
                chart.getAxeLayout().setXTickProvider(new StaticTickProvider(getRowsOfData(xMax)));
            } else {
                //preko 20 layera
                chart.getAxeLayout().setXTickProvider(new RegularTickProvider(20));
            }
        }
        if (prop.isyAxeInteger()) {
            chart.getAxeLayout().setYTickRenderer(new IntegerTickRenderer());
            int yMax = (int) prop.getyRange().getMax();
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
        chart.getAxeLayout().setXAxeLabel(prop.getxAxeLabel());
        chart.getAxeLayout().setYAxeLabel(prop.getyAxeLabel());
        chart.getAxeLayout().setZAxeLabel(prop.getzAxeLabel());
        if (prop.isxAxeInteger()) {
            chart.getAxeLayout().setXTickRenderer(new IntegerTickRenderer());
            int xMax = (int) prop.getxRange().getMax();
            if (xMax < 20) {
                chart.getAxeLayout().setXTickProvider(new StaticTickProvider(getRowsOfData(xMax)));
            } else {
                //preko 20 layera
                chart.getAxeLayout().setXTickProvider(new RegularTickProvider(20));
            }
        }
        if (prop.isyAxeInteger()) {
            chart.getAxeLayout().setYTickRenderer(new IntegerTickRenderer());
            int yMax = (int) prop.getyRange().getMax();
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

    private static float[] getRowsOfData(int l) {
        float[] rows = new float[l];

        for (int i = 0; i < l; i++) {
            rows[i] = i + 1;
        }
        return rows;
    }

}

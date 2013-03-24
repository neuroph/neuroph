package org.nugs.graph3d;

import java.beans.Beans;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;

/**
 *
 * @author vedrana
 */
public class Surface3DFactory {

    /**
     * Create a surface chart and open it on Chart launcher
     *
     * @param mapper function of chart
     * @param xRange x axes range of function
     * @param yRange y axes range of function
     * @param xSteps steps of function on x values
     * @param ySteps steps of function on y values
     * @param prop properties
     * @return void
     */
    public static Chart createSurface(Mapper mapper, Range xRange, Range yRange, Surface3DProperties prop) {
        Beans.setDesignTime(false);
       
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(xRange, prop.getxSteps(), yRange, prop.getySteps()), mapper);
        surface.setColorMapper(new ColorMapper(prop.getChartColor(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(prop.isChartWireframed());
        surface.setWireframeColor(prop.getWireframeColor());
       
        // Create a chart and add surface
        Chart chart = new Chart(prop.getChartQuality());
        chart.getScene().getGraph().add(surface);
        return chart;
    }

    /**
     * Create a surface chart with default properties and open it on Chart launcher
     *
     * @param mapper function of chart
     * @param xRange x axes range of function
     * @param yRange y axes range of function
     * @param xSteps steps of function on x values
     * @param ySteps steps of function on y values
     * @return void
     */
    public static Chart createSurface(Mapper mapper, Range xRange, Range yRange) {
        return createSurface(mapper, xRange, yRange, new Surface3DProperties());
    }
}

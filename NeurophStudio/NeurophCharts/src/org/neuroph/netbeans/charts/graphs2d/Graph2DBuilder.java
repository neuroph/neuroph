package org.neuroph.netbeans.charts.graphs2d;

import org.jfree.chart.ChartPanel;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph2d.api.DataProvider2D;

/**
 *
 * @author Vedrana Gajic
 */
public abstract class Graph2DBuilder {

    protected Attribute attribute1;
    protected Attribute attribute2;
    protected DataProvider2D provider2D;

    public abstract ChartPanel create();

    public void setAttribute2(Attribute attribute2) {
        this.attribute2 = attribute2;
    }

    public Attribute getAttribute2() {
        return attribute2;
    }

    public void setAttribute1(Attribute attribute1) {
        this.attribute1 = attribute1;
    }

    public Attribute getAttribute1() {
        return attribute1;
    }

    public void setProvider2D(DataProvider2D provider2D) {
        this.provider2D = provider2D;
    }

    public DataProvider2D getProvider2D() {
        return provider2D;
    }
}

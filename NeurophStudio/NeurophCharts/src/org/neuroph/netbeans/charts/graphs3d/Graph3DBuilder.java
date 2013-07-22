package org.neuroph.netbeans.charts.graphs3d;

import org.jzy3d.chart.Chart;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.DataProvider3D;

/**
 * Base class for all 3D graph builders
 * Override createGraph method to create specific types of graphs
 * 
 * @author Vedrana Gajic
 */
public abstract class Graph3DBuilder {

    protected Attribute attribute1;
    protected Attribute attribute2;
    protected Attribute attribute3;
    protected DataProvider3D dataProvider3D;

    public abstract Chart createGraph();

    public void setAttribute1(Attribute attribute1) {
        this.attribute1 = attribute1;
    }

    public Attribute getAttribute1() {
        return attribute1;
    }

    public void setAttribute2(Attribute attribute2) {
        this.attribute2 = attribute2;
    }

    public Attribute getAttribute2() {
        return attribute2;
    }

    public void setAttribute3(Attribute attribute3) {
        this.attribute3 = attribute3;
    }

    public Attribute getAttribute3() {
        return attribute3;
    }

    public void setProvider3D(DataProvider3D provider) {
        this.dataProvider3D = provider;
    }

    public DataProvider3D getProvider3D() {
        return dataProvider3D;
    }
}

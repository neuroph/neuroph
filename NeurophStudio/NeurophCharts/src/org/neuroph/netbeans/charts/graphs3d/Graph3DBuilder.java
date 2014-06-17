package org.neuroph.netbeans.charts.graphs3d;

import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.DataProvider3D;

/**
 * Base class for all 3D graph builders
 * Override createGraph method to create specific types of graphs
 * 
 * C - graph panel class that will be created and returned by factory methods 
 * P - class that is used to represent 3D point 
 * 
 * @author Vedrana Gajic
 */
public abstract class Graph3DBuilder<C, P> {

    protected Attribute attribute1;
    protected Attribute attribute2;
    protected Attribute attribute3;
    protected DataProvider3D<P> dataProvider3D;

    public abstract C createGraph();

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

    public void setDataProvider(DataProvider3D<P> provider) {
        this.dataProvider3D = provider;
    }

    public DataProvider3D<P> getDataProvider() {
        return dataProvider3D;
    }
}

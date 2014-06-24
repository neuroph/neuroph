/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.charts.graphs;

import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.charts.graphs3d.Graph3DBuilder;
import org.neuroph.netbeans.jmevisualization.JMEVisualization;
import org.neuroph.netbeans.jmevisualization.charts.providers.DatasetDataProvider3D;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.Point3D;

/**
 *
 * @author Milos Randjic
 */
public class JMEDatasetHistogram3D extends Graph3DBuilder<Void, Point3D.Float>{
    
    private DataSet dataset;
    private JMEVisualization jmeVisualization;
    
    public JMEDatasetHistogram3D(DataSet dataset, JMEVisualization jmeVisualization) {
        super();
        dataProvider3D = new DatasetDataProvider3D(dataset);
        this.dataset = dataset;
        this.jmeVisualization = jmeVisualization;
    }
    
    public DataSet getDataset() {
        return dataset;
    }

    public void setDataset(DataSet dataset) {
        this.dataset = dataset;
    }
    
    @Override
    public String toString() {
        return "Histogram";
    }

    @Override
    public Void createGraph() {
        setAttribute1(new Attribute(0, false, "Attribute"));
        setAttribute2(new Attribute(1, false, "Attribute"));
        setAttribute3(new Attribute(2, false, "Attribute"));
        
        // get points to display
        Point3D.Float[] points3D = (Point3D.Float[]) dataProvider3D.getData(attribute1, attribute2, attribute3);
        
        // create jme scatter graph with these points
        JMEHistogram3DFactory jmeHistogramFactory = new JMEHistogram3DFactory(jmeVisualization);
        jmeHistogramFactory.createHistogram3D(points3D);
        return null;
    }
    
}

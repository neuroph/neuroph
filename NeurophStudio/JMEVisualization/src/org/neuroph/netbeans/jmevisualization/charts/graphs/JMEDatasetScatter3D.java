package org.neuroph.netbeans.jmevisualization.charts.graphs;

import org.neuroph.netbeans.jmevisualization.charts.providers.DatasetDataProvider3D;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.charts.graphs3d.Graph3DBuilder;
import org.neuroph.netbeans.jmevisualization.JMEVisualization;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.Point3D;
import org.nugs.graph3d.api.Scatter3DProperties;

/**
 * Uses JMEScatter3DFactory to create scatter graph for dataset
 * 
 * @author Milos Randjic
 */
public class JMEDatasetScatter3D extends Graph3DBuilder<Void, Point3D.Float>{

    private DataSet dataset;
    private Scatter3DProperties properties;
    private JMEVisualization jmeVisualization;
    
    public JMEDatasetScatter3D(DataSet dataset, JMEVisualization jmeVisualization) {
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
    
     public Scatter3DProperties getProperties() {
        return properties;
    }

    public void setProperties(Scatter3DProperties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Scatter";
    }
        
    @Override
    public Void createGraph() { 
        setAttribute1(new Attribute(0, false, "Attribute"));
        setAttribute2(new Attribute(1, false, "Attribute"));
        setAttribute3(new Attribute(2, false, "Attribute"));
        
        properties = new Scatter3DProperties();
        properties.setDotSize(1f);
        properties.setxAxeLabel(attribute1.getLabel());
        properties.setyAxeLabel(attribute2.getLabel());
        properties.setzAxeLabel(attribute3.getLabel());  

        // get points to display
        Point3D.Float[] points3D = (Point3D.Float[]) dataProvider3D.getData(attribute1, attribute2, attribute3);       
        
        // create jme scatter graph with these points
        JMEScatter3DFactory jmeScatterFactory = new JMEScatter3DFactory(jmeVisualization);
        jmeScatterFactory.createScatter3D(points3D);
        
        return null;
    }   
}
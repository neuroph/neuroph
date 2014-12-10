package org.neuroph.netbeans.jmevisualization.charts.graphs;

import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.charts.graphs3d.Graph3DBuilder;
import org.neuroph.netbeans.jmevisualization.JMEVisualization;
import org.neuroph.netbeans.jmevisualization.charts.JMEVisualizationTopComponent;
import org.neuroph.netbeans.jmevisualization.charts.providers.DatasetDataProvider3D;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.Point3D;
import org.nugs.graph3d.api.Scatter3DProperties;

/**
 * Uses JMEScatter3DFactory to create scatter graph for dataset
 *
 * @author Milos Randjic
 */
public class JMEDatasetScatter3D extends Graph3DBuilder<Void, Point3D.Float> {

    private DataSet dataset;
    private int[] inputs;
    private ArrayList<ColorRGBA> dominantOutputColors;
    private JMEVisualization jmeVisualization;
    private Scatter3DProperties properties;
    private JMEScatter3DFactory jmeScatterFactory;
    private Point3D.Float[] points3D;

    public JMEDatasetScatter3D(DataSet dataset, JMEVisualization jmeVisualization) {
        super();
        dataProvider3D = new DatasetDataProvider3D(dataset);
        this.dataset = dataset;
        this.jmeVisualization = jmeVisualization;

    }

    public JMEDatasetScatter3D(DataSet dataset, int[] inputs, ArrayList<ColorRGBA> dominantOutputColors, JMEVisualization jmeVisualization) {
        super();
        dataProvider3D = new DatasetDataProvider3D(dataset);
        this.dataset = dataset;
        this.inputs = inputs;
        this.dominantOutputColors = dominantOutputColors;
        this.jmeVisualization = jmeVisualization;

    }

    public int[] getInputs() {
        return inputs;
    }

    public void setInputs(int[] inputs) {
        this.inputs = inputs;
    }

    public JMEVisualization getJmeVisualization() {
        return jmeVisualization;
    }

    public void setJmeVisualization(JMEVisualization jmeVisualization) {
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

    /**
     * Creates graph for chosen inputs
     *
     * @return
     */
    @Override
    public Void createGraph() {

        /*
         Set attributes for chosen inputs
         */
        setAttribute1(new Attribute(inputs[0], false, "Attribute"));
        setAttribute2(new Attribute(inputs[1], false, "Attribute"));
        setAttribute3(new Attribute(inputs[2], false, "Attribute"));

        /*
         Get points for chosen attributes
         */
        points3D = (Point3D.Float[]) dataProvider3D.getData(attribute1, attribute2, attribute3);

        /*
         Define properties for scatter graph
         */
        properties = new Scatter3DProperties();
        properties.setDotSize(1f);
        properties.setXAxeLabel(attribute1.getLabel());
        properties.setYAxeLabel(attribute2.getLabel());
        properties.setZAxeLabel(attribute3.getLabel());
        properties.setPointColors(dominantOutputColors);

        /*
         Instantiate jmeScatterFactory in order to create graph
         */
        jmeScatterFactory = new JMEScatter3DFactory(jmeVisualization);
        jmeScatterFactory.createScatter3D(points3D, properties);

        //JMEVisualizationTopComponent.findInstance().getVisualizationPanel().revalidate();
        
        return null;
    }

}

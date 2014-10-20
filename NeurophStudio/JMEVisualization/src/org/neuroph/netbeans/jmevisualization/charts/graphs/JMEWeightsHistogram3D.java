/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.charts.graphs;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.charts.graphs3d.Graph3DBuilder;
import org.neuroph.netbeans.jmevisualization.JMEVisualization;
import org.neuroph.netbeans.jmevisualization.charts.JMEVisualizationTopComponent;
import org.neuroph.netbeans.jmevisualization.charts.providers.WeightsDataProvider3D;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.Histogram3DProperties;
import org.nugs.graph3d.api.Point3D;

/**
 *
 * @author Milos Randjic
 */
public class JMEWeightsHistogram3D extends Graph3DBuilder<Void, Point3D.Float> {

    private NeuralNetwork neuralNetwork;
    private JMEVisualization jmeVisualization;
    private Histogram3DProperties properties;
    private JMEHistogram3DFactory jmeHistogramFactory;

    public JMEWeightsHistogram3D(NeuralNetwork neuralNetwork, JMEVisualization jmeVisualization) {
        super();
        dataProvider3D = new WeightsDataProvider3D(neuralNetwork);
        this.neuralNetwork = neuralNetwork;
        this.jmeVisualization = jmeVisualization;

    }

    public JMEWeightsHistogram3D(JMEVisualization jmeVisualization) {
        super();
        this.jmeVisualization = jmeVisualization;

    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        dataProvider3D = new WeightsDataProvider3D(this.neuralNetwork);
    }

    public JMEVisualization getJmeVisualization() {
        return jmeVisualization;
    }

    public void setJmeVisualization(JMEVisualization jmeVisualization) {
        this.jmeVisualization = jmeVisualization;
    }

    public Histogram3DProperties getProperties() {
        return properties;
    }

    public void setProperties(Histogram3DProperties properties) {
        this.properties = properties;
    }

    public JMEHistogram3DFactory getJmeHistogramFactory() {
        return jmeHistogramFactory;
    }

    public void setJmeHistogramFactory(JMEHistogram3DFactory jmeHistogramFactory) {
        this.jmeHistogramFactory = jmeHistogramFactory;
    }

    @Override
    public String toString() {
        return "Weights Histogram";
    }

    /**
     * Creates graph for chosen inputs
     *
     * @return
     */
    @Override
    public Void createGraph() {

        /*
         Set attributes for histogram
         */
        setAttribute1(new Attribute(0, false, "Attribute"));
        setAttribute2(new Attribute(1, false, "Attribute"));
        setAttribute3(new Attribute(2, false, "Attribute"));

        /*
         Get points for chosen attributes
         */
        Point3D.Float[] points3D = (Point3D.Float[]) dataProvider3D.getData(attribute1, attribute2, attribute3);

        /*
         Find maximum number of bars per row
         */
        int maxBarsSize = -1;
        for (int i = 0; i < neuralNetwork.getLayersCount(); i++) {
            Layer l = neuralNetwork.getLayerAt(i);
            if (l.getNeuronsCount() > maxBarsSize) {
                maxBarsSize = l.getNeuronsCount();
            }
        }

        /*
         Define properties for histogram graph
         */
        properties = new Histogram3DProperties();
        properties.setRadius(2f);
        properties.setMaxBarsSize(maxBarsSize);
        properties.setNumberOfBarRows(neuralNetwork.getLayersCount());
        
        if (!this.jmeVisualization.isRotated()) {
            this.jmeVisualization.getRootNode().rotate(-1.57f, -1.57f, 0.0f);
            this.jmeVisualization.setRotated(true);
        }

        /*
         Instantiate jmeHistogramFactory in order to create graph
         */
        jmeHistogramFactory = new JMEHistogram3DFactory(jmeVisualization);
        jmeHistogramFactory.createHistogram3D(points3D, properties);
        
        //JMEVisualizationTopComponent.findInstance().getVisualizationPanel().revalidate();
        
        return null;
    }

}

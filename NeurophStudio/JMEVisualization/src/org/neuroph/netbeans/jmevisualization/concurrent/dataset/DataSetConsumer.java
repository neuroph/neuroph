/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.concurrent.dataset;

import org.neuroph.netbeans.jmevisualization.DataSetVisualizationParameters;
import org.neuroph.netbeans.jmevisualization.JMEVisualization;
import org.neuroph.netbeans.jmevisualization.charts.graphs.JMEDatasetScatter3D;
import org.neuroph.netbeans.jmevisualization.concurrent.Consumer;

/**
 *
 * @author Milos Randjic
 */
public class DataSetConsumer extends Consumer {

    public DataSetConsumer(JMEVisualization jmeVisualization) {
        super(jmeVisualization);
    }

    @Override
    public void run() {
        while (true) {
            try {

                /*
                 Fetch parameters for drawing from sharedQueue
                 If sharedQueue is empty, then consumer has to wait until the first object in sharedQueue appears
                 */
                DataSetVisualizationParameters parameters = (DataSetVisualizationParameters) getSharedQueue().take();

                /*
                 Draw a scatter graph, for given parameters
                 */
                JMEDatasetScatter3D scatter = new JMEDatasetScatter3D(parameters.getDataSet(), parameters.getInputs(), parameters.getDominantOutputColors(), getJmeVisualization());
                scatter.createGraph();

            } catch (InterruptedException ex) {
            }
            
        }       
    }

}
